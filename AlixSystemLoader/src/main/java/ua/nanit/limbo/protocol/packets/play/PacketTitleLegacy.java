package ua.nanit.limbo.protocol.packets.play;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTitle;
import net.kyori.adventure.text.Component;
import ua.nanit.limbo.protocol.packets.retrooper.OutRetrooperPacket;
import ua.nanit.limbo.server.data.Title;

public class PacketTitleLegacy extends OutRetrooperPacket<WrapperPlayServerTitle> {

    public PacketTitleLegacy() {
        super(WrapperPlayServerTitle.class);
    }

    public PacketTitleLegacy setAction(WrapperPlayServerTitle.TitleAction action) {
        this.wrapper().setAction(action);
        return this;
    }

    public PacketTitleLegacy setTitle(Title title) {
        this.wrapper().setTitle(Component.text(title.getTitle()));
        this.wrapper().setSubtitle(Component.text(title.getSubtitle()));
        this.wrapper().setFadeInTicks(title.getFadeIn());
        this.wrapper().setStayTicks(title.getStay());
        this.wrapper().setFadeOutTicks(title.getFadeOut());
        return this;
    }
}
