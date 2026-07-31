package ua.nanit.limbo.protocol.packets.play;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import net.kyori.adventure.text.Component;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;

public class PacketTitleSetTitle extends OutRetrooperPacket<WrapperPlayServerSetTitleText> {

    public PacketTitleSetTitle() {
        super(WrapperPlayServerSetTitleText.class);
    }

    public PacketTitleSetTitle setTitle(String title) {
        this.wrapper().setTitle(Component.text(title));
        return this;
    }
}
