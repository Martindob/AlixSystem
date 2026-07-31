package ua.nanit.limbo.protocol.packets.play;

import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.registry.Version;

public class PacketTitleTimes implements PacketOut {

    private int fadeIn;
    private int stay;
    private int fadeOut;

    public PacketTitleTimes setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public PacketTitleTimes setStay(int stay) {
        this.stay = stay;
        return this;
    }

    public PacketTitleTimes setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeInt(fadeIn);
        msg.writeInt(stay);
        msg.writeInt(fadeOut);
    }
}
