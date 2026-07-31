package ua.nanit.limbo.protocol.packets.login;

import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.registry.Version;

public class PacketLoginPluginRequest implements PacketOut {

    private int messageId;
    private String channel;
    private byte[] data;

    public PacketLoginPluginRequest setMessageId(int messageId) {
        this.messageId = messageId;
        return this;
    }

    public PacketLoginPluginRequest setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public PacketLoginPluginRequest setData(byte[] data) {
        this.data = data;
        return this;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeVarInt(messageId);
        msg.writeString(channel);
        msg.writeBytes(data);
    }

    public static PacketLoginPluginRequest of(int messageId, String channel, byte[] data) {
        return new PacketLoginPluginRequest().setMessageId(messageId).setChannel(channel).setData(data);
    }
}
