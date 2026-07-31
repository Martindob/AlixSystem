package ua.nanit.limbo.commands;

import alix.common.packets.command.CommandsWrapperConstructor;
import alix.common.packets.command.CustomCommand;
import alix.common.utils.netty.WrapperTransformer;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.netty.buffer.ByteBuf;
import ua.nanit.limbo.protocol.ByteMessage;
import ua.nanit.limbo.protocol.packets.play.PacketOutCommands;
import ua.nanit.limbo.protocol.registry.Version;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshot;

import java.util.List;

final class LimboMultiCommandImpl implements LimboCommand {

    private final PacketSnapshot snapshot;
    private final List<CustomCommand> commands;

    LimboMultiCommandImpl(List<CustomCommand> commands) {
        this.commands = commands;
        this.snapshot = PacketSnapshot.of(new PacketOutCommands(this));
    }

    @Override
    public void encode(ByteMessage msg, Version version) {
        ServerVersion ver = version.getRetrooperVersion();
        ByteBuf encodedNoId = CommandsWrapperConstructor.constructMultipleArgs(commands, WrapperTransformer.DYNAMIC_NO_ID, ver);

        msg.writeBytes(encodedNoId, 0, encodedNoId.readableBytes());
        encodedNoId.release();
    }

    @Override
    public PacketSnapshot getPacketSnapshot() {
        return this.snapshot;
    }
}