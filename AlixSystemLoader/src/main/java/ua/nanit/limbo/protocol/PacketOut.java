package ua.nanit.limbo.protocol;

import ua.nanit.limbo.protocol.registry.Version;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshot;

public interface PacketOut extends Packet {

    @Override
    default void decode(ByteMessage msg, Version version) {
        // Can be ignored for outgoing packets
    }

    default PacketSnapshot toSnapshot() {
        return PacketSnapshot.of(this);
    }
}
