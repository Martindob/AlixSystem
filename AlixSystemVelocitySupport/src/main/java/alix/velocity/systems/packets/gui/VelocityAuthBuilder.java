package alix.velocity.systems.packets.gui;

import alix.common.messages.Messages;
import alix.velocity.utils.user.VerifiedUser;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import io.netty.channel.Channel;
import ua.nanit.limbo.connection.login.gui.bedrock.AbstractAuthBuilder;
import ua.nanit.limbo.connection.login.packets.SoundPackets;
import ua.nanit.limbo.connection.pipeline.encryption.CipherHandler;
import ua.nanit.limbo.protocol.PacketOut;
import ua.nanit.limbo.protocol.packets.PacketUtils;
import ua.nanit.limbo.protocol.registry.Version;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshot;

import java.util.function.Consumer;

public final class VelocityAuthBuilder extends AbstractAuthBuilder {

    private final VerifiedUser user;

    //packet writing
    private final Channel channel;
    private final Version version;
    private final CipherHandler cipher;

    public VelocityAuthBuilder(VerifiedUser user, Consumer<Boolean> onConfirm, boolean includeLeaveButton) {
        super(user.getData().tokenKey(), onConfirm, includeLeaveButton);
        this.user = user;
        this.channel = user.getChannel();
        this.version = Version.of(user.user.getClientVersion().getProtocolVersion());
        this.cipher = CipherHandler.encryptionFor(this.channel);
    }

    @Override
    public void onCloseAttempt() {
        this.user.getDuplexProcessor().endQRCodeShow();
    }

    private static final String
            accessConfirmedMessagePacket = Messages.getWithPrefix("google-auth-access-confirmed"),
            accessDeniedMessagePacket = Messages.getWithPrefix("google-auth-access-confirmation-failed");

    public static void visualsOnProvenAccess(VelocityAuthBuilder builder, VerifiedUser user) {
        builder.write(SoundPackets.PLAYER_LEVELUP);
        user.sendMessage(accessConfirmedMessagePacket);
    }

    public static void visualsOnDeniedAccess(VelocityAuthBuilder builder, VerifiedUser user) {
        builder.write(SoundPackets.ITEM_BREAK);
        user.sendMessage(accessDeniedMessagePacket);
    }

    @Override
    protected void write(PacketOut packet) {
        PacketUtils.write(this.channel, this.version, packet, this.cipher);
    }

    @Override
    protected void writeAndFlush(PacketOut packet) {
        PacketUtils.writeAndFlush(this.channel, this.version, packet, this.cipher);
    }

    @Override
    protected void sendPacketAndClose(PacketSnapshot packet) {
        PacketUtils.closeWith(this.channel, this.version, packet, this.cipher);
    }

    @Override
    protected ClientVersion getClientVersion() {
        return this.version.getClientVersion();
    }
}