package ua.nanit.limbo.protocol.packets.play.keepalive;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import ua.nanit.limbo.connection.ClientConnection;
import ua.nanit.limbo.protocol.packets.retrooper.InRetrooperPacket;
import ua.nanit.limbo.server.LimboServer;

public class PacketInPlayKeepAlive extends InRetrooperPacket<WrapperPlayClientKeepAlive> {

    public PacketInPlayKeepAlive() {
        super(WrapperPlayClientKeepAlive.class);
    }

    public void setId(long id) {
        this.wrapper().setId(id);
    }

    @Override
    public void handle(ClientConnection conn, LimboServer server) {
        conn.getVerifyState().handle(this);
    }
}
