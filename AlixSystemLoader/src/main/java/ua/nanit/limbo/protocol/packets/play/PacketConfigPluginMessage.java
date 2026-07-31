package ua.nanit.limbo.protocol.packets.play;

import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.registry.Version;

public class PacketConfigPluginMessage implements PacketOut {

    private String channel;
    private String message;

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeString(channel);
        msg.writeString(message);
    }
}
