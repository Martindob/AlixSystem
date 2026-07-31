package ua.nanit.limbo.protocol.packets.play;

import alix.common.packets.message.MessageWrapper;
import alix.common.utils.formatter.AlixFormatter;
import alix.common.utils.netty.WrapperUtils;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshot;
import ua.nanit.limbo.protocol.registry.Version;

public final class PacketPlayOutMessage implements PacketOut {

    private String message;

    public PacketPlayOutMessage() {
    }

    public static PacketSnapshot snapshot(String message) {
        return withMessage(message).toSnapshot();
    }

    public static PacketPlayOutMessage withMessage(String message) {
        return new PacketPlayOutMessage().setMessage(AlixFormatter.translateColors(message));
    }

    public PacketPlayOutMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public PacketWrapper<?> packetWrapper(Version version) {
        return MessageWrapper.createWrapper(Component.empty(), false, version.getRetrooperVersion());
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        var retrooperVersion = version.getRetrooperVersion();
        var wrapper = MessageWrapper.createWrapper(this.message, false, retrooperVersion);
        WrapperUtils.writeNoID(wrapper, msg.getBuf(), retrooperVersion);
    }
}
