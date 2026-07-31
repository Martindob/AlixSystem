package ua.nanit.limbo.protocol.packets.play.keepalive;

import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.PacketIn;
import ua.nanit.limbo.protocol.registry.Version;

public class PacketInConfigKeepAlive implements PacketIn {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void decode(ByteMessage msg, Version version) {
        if (version.moreOrEqual(Version.V1_12_2)) {
            this.id = msg.readLong();
        } else if (version.moreOrEqual(Version.V1_8)) {
            this.id = msg.readVarInt();
        } else {
            this.id = msg.readInt();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
