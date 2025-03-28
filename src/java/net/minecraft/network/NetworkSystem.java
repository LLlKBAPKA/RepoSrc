package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.minecraft.client.network.handshake.ClientHandshakeNetHandler;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.network.handshake.ServerHandshakeNetHandler;
import net.minecraft.network.play.server.SDisconnectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NetworkSystem {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final LazyValue<NioEventLoopGroup> SERVER_NIO_EVENTLOOP = new LazyValue<>(() ->
    {
        return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
    });
    public static final LazyValue<EpollEventLoopGroup> SERVER_EPOLL_EVENTLOOP = new LazyValue<>(() ->
    {
        return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
    });

    /**
     * Reference to the MinecraftServer object.
     */
    private final MinecraftServer server;

    /**
     * True if this NetworkSystem has never had his endpoints terminated
     */
    public volatile boolean isAlive;
    private final List<ChannelFuture> endpoints = Collections.synchronizedList(Lists.newArrayList());
    private final List<NetworkManager> networkManagers = Collections.synchronizedList(Lists.newArrayList());

    public NetworkSystem(MinecraftServer server) {
        this.server = server;
        this.isAlive = true;
    }

    /**
     * Adds a channel that listens on publicly accessible network ports
     */
    public void addEndpoint(@Nullable InetAddress address, int port) throws IOException {
        synchronized (this.endpoints) {
            Class<? extends ServerSocketChannel> oclass;
            LazyValue<? extends EventLoopGroup> lazyvalue;

            if (Epoll.isAvailable() && this.server.shouldUseNativeTransport()) {
                oclass = EpollServerSocketChannel.class;
                lazyvalue = SERVER_EPOLL_EVENTLOOP;
                LOGGER.info("Using epoll channel type");
            } else {
                oclass = NioServerSocketChannel.class;
                lazyvalue = SERVER_NIO_EVENTLOOP;
                LOGGER.info("Using default channel type");
            }

            this.endpoints.add((new ServerBootstrap()).channel(oclass).childHandler(new ChannelInitializer<Channel>() {
                protected void initChannel(Channel p_initChannel_1_) throws Exception {
                    try {
                        p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, true);
                    } catch (ChannelException channelexception) {
                    }

                    p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("legacy_query", new LegacyPingHandler(NetworkSystem.this)).addLast("splitter", new NettyVarint21FrameDecoder()).addLast("decoder", new NettyPacketDecoder(PacketDirection.SERVERBOUND)).addLast("prepender", new NettyVarint21FrameEncoder()).addLast("encoder", new NettyPacketEncoder(PacketDirection.CLIENTBOUND));
                    int i = NetworkSystem.this.server.func_241871_k();
                    NetworkManager networkmanager = i > 0 ? new RateLimitedNetworkManager(i) : new NetworkManager(PacketDirection.SERVERBOUND);
                    NetworkSystem.this.networkManagers.add(networkmanager);
                    p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
                    networkmanager.setNetHandler(new ServerHandshakeNetHandler(NetworkSystem.this.server, networkmanager));
                }
            }).group(lazyvalue.getValue()).localAddress(address, port).bind().syncUninterruptibly());
        }
    }

    /**
     * Adds a channel that listens locally
     */
    public SocketAddress addLocalEndpoint() {
        ChannelFuture channelfuture;

        synchronized (this.endpoints) {
            channelfuture = (new ServerBootstrap()).channel(LocalServerChannel.class).childHandler(new ChannelInitializer<Channel>() {
                protected void initChannel(Channel p_initChannel_1_) throws Exception {
                    NetworkManager networkmanager = new NetworkManager(PacketDirection.SERVERBOUND);
                    networkmanager.setNetHandler(new ClientHandshakeNetHandler(NetworkSystem.this.server, networkmanager));
                    NetworkSystem.this.networkManagers.add(networkmanager);
                    p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
                }
            }).group(SERVER_NIO_EVENTLOOP.getValue()).localAddress(LocalAddress.ANY).bind().syncUninterruptibly();
            this.endpoints.add(channelfuture);
        }

        return channelfuture.channel().localAddress();
    }

    /**
     * Shuts down all open endpoints (with immediate effect?)
     */
    public void terminateEndpoints() {
        this.isAlive = false;

        for (ChannelFuture channelfuture : this.endpoints) {
            try {
                channelfuture.channel().close().sync();
            } catch (InterruptedException interruptedexception) {
                LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }

    /**
     * Will try to process the packets received by each NetworkManager, gracefully manage processing failures and cleans
     * up dead connections
     */
    public void tick() {
        synchronized (this.networkManagers) {
            Iterator<NetworkManager> iterator = this.networkManagers.iterator();

            while (iterator.hasNext()) {
                NetworkManager networkmanager = iterator.next();

                if (!networkmanager.hasNoChannel()) {
                    if (networkmanager.isChannelOpen()) {
                        try {
                            networkmanager.tick();
                        } catch (Exception exception) {
                            if (networkmanager.isLocalChannel()) {
                                throw new ReportedException(CrashReport.makeCrashReport(exception, "Ticking memory connection"));
                            }

                            LOGGER.warn("Failed to post packet for {}", networkmanager.getRemoteAddress(), exception);
                            ITextComponent itextcomponent = new StringTextComponent("Internal server error");
                            networkmanager.sendPacket(new SDisconnectPacket(itextcomponent), (p_210474_2_) ->
                            {
                                networkmanager.closeChannel(itextcomponent);
                            });
                            networkmanager.disableAutoRead();
                        }
                    } else {
                        iterator.remove();
                        networkmanager.handleDisconnection();
                    }
                }
            }
        }
    }

    public MinecraftServer getServer() {
        return this.server;
    }
}
