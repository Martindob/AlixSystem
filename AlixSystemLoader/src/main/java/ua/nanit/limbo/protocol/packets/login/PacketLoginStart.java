package ua.nanit.limbo.protocol.packets.login;

import com.github.retrooper.packetevents.util.crypto.SignatureData;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientLoginStart;
import ua.nanit.limbo.connection.ClientConnection;
import ua.nanit.limbo.protocol.packets.retrooper.InRetrooperPacket;
import ua.nanit.limbo.server.LimboServer;

import java.util.UUID;

public final class PacketLoginStart extends InRetrooperPacket<WrapperLoginClientLoginStart> {

    public PacketLoginStart() {
        super(WrapperLoginClientLoginStart.class);
    }

    public String getUsername() {
        return this.wrapper().getUsername();
    }

    public SignatureData getSignatureData() {
        return this.wrapper().getSignatureData().orElse(null);
    }

    public UUID getUUID() {
        return this.wrapper().getPlayerUUID().orElse(null);
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        server.getPacketHandler().handle(conn, this);
    }
}