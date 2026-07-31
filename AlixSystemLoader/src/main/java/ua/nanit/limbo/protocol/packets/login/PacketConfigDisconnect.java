package ua.nanit.limbo.protocol.packets.login;

import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerDisconnect;
import net.kyori.adventure.text.Component;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshot;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

public class PacketConfigDisconnect extends OutRetrooperPacket<WrapperConfigServerDisconnect> {

    public PacketConfigDisconnect() {
        super(WrapperConfigServerDisconnect.class);
    }

    public PacketConfigDisconnect setReason(String reason) {
        this.wrapper().setReason(Component.text(reason));
        return this;
    }

    public static PacketSnapshot snapshot(String reason) {
        return new PacketConfigDisconnect().setReason(reason).toSnapshot();
    }
}
