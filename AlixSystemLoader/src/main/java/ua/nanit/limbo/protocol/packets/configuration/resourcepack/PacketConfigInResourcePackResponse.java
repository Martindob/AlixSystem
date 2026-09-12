package ua.nanit.limbo.protocol.packets.configuration.resourcepack;

import alix.common.utils.netty.safety.NettySafety;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientResourcePackStatus;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientResourcePackStatus.Result;
import ua.nanit.limbo.NanoLimbo;
import ua.nanit.limbo.connection.ClientConnection;
import ua.nanit.limbo.protocol.packets.retrooper.InRetrooperPacket;
import ua.nanit.limbo.server.LimboServer;

public final class PacketConfigInResourcePackResponse extends InRetrooperPacket<WrapperConfigClientResourcePackStatus> {

    public PacketConfigInResourcePackResponse() {
        super(WrapperConfigClientResourcePackStatus.class);
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        if (!NanoLimbo.enableFingerprinting)
            throw NettySafety.INVALID_PACKET;

        Boolean hit = isHit(this.wrapper().getResult());
        if (hit == null) return;//an intermediate/non-terminal status - wait for the real outcome

        conn.onFingerprintProbeResponse(this.wrapper().getPackId(), hit);
    }

    /**
     * Only a FINAL outcome resolves a probe - see the caller. With a deliberately unreachable probe URL
     * (see FingerprintManager), SUCCESSFULLY_LOADED can only mean the client already had that exact content
     * cached (a hit); ACCEPTED/DOWNLOADED are intermediate steps of a genuine download attempt that can
     * never actually succeed against that URL, so they're not resolved here - the miss that follows them
     * (FAILED_DOWNLOAD) is.
     *
     * @return true (hit/cached), false (miss/genuinely failed), or null if not a terminal outcome yet
     */
    private static Boolean isHit(Result result) {
        return switch (result) {
            case SUCCESSFULLY_LOADED -> Boolean.TRUE;
            case DECLINED, FAILED_DOWNLOAD, INVALID_URL, FAILED_RELOAD, DISCARDED -> Boolean.FALSE;
            case ACCEPTED, DOWNLOADED -> null;
        };
    }
}
