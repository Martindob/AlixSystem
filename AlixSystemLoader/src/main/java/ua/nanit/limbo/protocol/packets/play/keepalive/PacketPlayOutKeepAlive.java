package ua.nanit.limbo.protocol.packets.play.keepalive;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

public final class PacketPlayOutKeepAlive extends OutRetrooperPacket<WrapperPlayServerKeepAlive> {

    public PacketPlayOutKeepAlive() {
        super(WrapperPlayServerKeepAlive.class);
    }

    public PacketPlayOutKeepAlive(int id) {
        this();
        this.setId(id);
    }

    public PacketPlayOutKeepAlive setId(long id) {
        this.wrapper().setId(id);
        return this;
    }
}
