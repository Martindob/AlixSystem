package ua.nanit.limbo.protocol.packets.play;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

public final class PacketPlayerAbilities extends OutRetrooperPacket<WrapperPlayServerPlayerAbilities> {

    public PacketPlayerAbilities() {
        super(WrapperPlayServerPlayerAbilities.class);
    }

    public PacketPlayerAbilities(WrapperPlayServerPlayerAbilities wrapper) {
        super(wrapper);
    }
}
