package ua.nanit.limbo.protocol.packets.login.disconnect;

import com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerDisconnect;
import net.kyori.adventure.text.Component;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshot;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

public class PacketLoginDisconnect extends OutRetrooperPacket<WrapperLoginServerDisconnect> {

    public PacketLoginDisconnect() {
        super(WrapperLoginServerDisconnect.class);
    }

    public PacketLoginDisconnect setReason(String reason) {
        this.setReason(Component.text(reason));
        return this;
    }

    public PacketLoginDisconnect setReason(Component reason) {
        this.wrapper().setReason(reason);
        return this;
    }

    public static PacketSnapshot snapshot(String reason) {
        return new PacketLoginDisconnect().setReason(reason).toSnapshot();
    }

    public static PacketSnapshot snapshot(Component reason) {
        return new PacketLoginDisconnect().setReason(reason).toSnapshot();
    }

}
