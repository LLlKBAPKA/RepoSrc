package net.minecraft.network.play.server;

import lombok.Getter;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;
import java.util.UUID;

@Getter
public class SChatPacket implements IPacket<IClientPlayNetHandler> {
    private ITextComponent chatComponent;
    private ChatType type;
    private UUID uuid;

    public SChatPacket() {
    }

    public SChatPacket(ITextComponent p_i232578_1_, ChatType p_i232578_2_, UUID p_i232578_3_) {
        this.chatComponent = p_i232578_1_;
        this.type = p_i232578_2_;
        this.uuid = p_i232578_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.chatComponent = buf.readTextComponent();
        this.type = ChatType.byId(buf.readByte());
        this.uuid = buf.readUniqueId();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeTextComponent(this.chatComponent);
        buf.writeByte(this.type.getId());
        buf.writeUniqueId(this.uuid);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(IClientPlayNetHandler handler) {
        handler.handleChat(this);
    }

    public ITextComponent getChatComponent() {
        return this.chatComponent;
    }

    /**
     * This method returns true if the type is SYSTEM or ABOVE_HOTBAR, and false if CHAT
     */
    public boolean isSystem() {
        return this.type == ChatType.SYSTEM || this.type == ChatType.GAME_INFO;
    }

    public ChatType getType() {
        return this.type;
    }

    public UUID func_240810_e_() {
        return this.uuid;
    }

    public boolean shouldSkipErrors() {
        return true;
    }
}
