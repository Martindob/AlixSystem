package ua.nanit.limbo.protocol.packets.play.keepalive;

import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.registry.Version;

public class PacketOutConfigKeepAlive implements PacketOut {

    private long id;

    public PacketOutConfigKeepAlive(int id) {
        this.id = id;
    }

    public PacketOutConfigKeepAlive() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            msg.writeLong(id);
        } else if (version.moreOrEqual(Version.V1_8)) {
            msg.writeVarInt((int) id);
        } else {
            msg.writeInt((int) id);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
