package ua.nanit.limbo.protocol.packets.play;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import net.kyori.adventure.text.Component;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

public class PacketTitleSetSubTitle extends OutRetrooperPacket<WrapperPlayServerSetTitleSubtitle> {

    public PacketTitleSetSubTitle() {
        super(WrapperPlayServerSetTitleSubtitle.class);
    }

    public PacketTitleSetSubTitle setSubtitle(String title) {
        this.wrapper().setSubtitle(Component.text(title));
        return this;
    }
}
