package net.minecraft.network.play.client;

import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;

import java.io.IOException;

public class CPlayerTryUseItemOnBlockPacket implements IPacket<IServerPlayNetHandler> {
    private BlockRayTraceResult field_218795_a;
    private Hand hand;

    public CPlayerTryUseItemOnBlockPacket() {
    }

    public CPlayerTryUseItemOnBlockPacket(Hand p_i50756_1_, BlockRayTraceResult p_i50756_2_) {
        this.hand = p_i50756_1_;
        this.field_218795_a = p_i50756_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.hand = buf.readEnumValue(Hand.class);
        this.field_218795_a = buf.readBlockRay();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.hand);
        buf.writeBlockRay(this.field_218795_a);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(IServerPlayNetHandler handler) {
        handler.processTryUseItemOnBlock(this);
    }

    public Hand getHand() {
        return this.hand;
    }

    public BlockRayTraceResult func_218794_c() {
        return this.field_218795_a;
    }
}
