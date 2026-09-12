package ua.nanit.limbo.protocol.packets.configuration.resourcepack;

import alix.common.utils.netty.safety.NettySafety;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientResourcePackStatus;
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

        Boolean hit = isHit(this.wrapper().getResult().name());
        if (hit == null) return;//an intermediate/non-terminal status (e.g. "ACCEPTED") - wait for the real outcome

        conn.onFingerprintProbeResponse(this.wrapper().getId(), hit);
    }

    /**
     * Only a FINAL outcome resolves a probe - see the caller. Matched by name rather than by enum constant
     * since the exact set of ResourcePackStatus values has changed across packetevents/protocol versions;
     * this only needs to recognize the shape of the name, not one specific version's exact constants.
     *
     * @return true (hit/cached), false (miss/genuinely failed), or null if this status isn't terminal yet
     */
    private static Boolean isHit(String resultName) {
        String r = resultName.toUpperCase();
        if (r.contains("SUCCESS") || r.contains("LOADED")) return Boolean.TRUE;
        if (r.contains("FAIL") || r.contains("DECLIN") || r.contains("INVALID") || r.contains("DISCARD")) return Boolean.FALSE;
        return null;
    }
}
