package ua.nanit.limbo.protocol.packets.play;

import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.NbtMessage;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.registry.Version;

public class PacketPlayerListHeader implements PacketOut {

    private NbtMessage header;
    private NbtMessage footer;

    public void setHeader(NbtMessage header) {
        this.header = header;
    }

    public void setFooter(NbtMessage footer) {
        this.footer = footer;
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        msg.writeNbtMessage(header, version);
        msg.writeNbtMessage(footer, version);
    }
}
