package ua.nanit.limbo.protocol.packets.play.payload;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

import java.nio.charset.StandardCharsets;

public final class PacketPlayOutPluginMessage extends OutRetrooperPacket<WrapperPlayServerPluginMessage> {

    public PacketPlayOutPluginMessage() {
        super(WrapperPlayServerPluginMessage.class);
    }

    public PacketPlayOutPluginMessage(WrapperPlayServerPluginMessage wrapper) {
        super(wrapper);
    }

    public void setChannel(String channel) {
        this.wrapper().setChannelName(channel);
    }

    public String getChannel() {
        return this.wrapper().getChannelName();
    }

    public void setMessage(String message) {
        this.wrapper().setData(message.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] getData() {
        return this.wrapper().getData();
    }
}