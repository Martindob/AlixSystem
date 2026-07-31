package ua.nanit.limbo.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import ua.nanit.limbo.NanoLimbo;
import ua.nanit.limbo.connection.ClientConnection;
import ua.nanit.limbo.connection.UnsafeCloseFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public final class Connections {

    private final Map<ChannelId, ClientConnection> connections;
    private final LongAdder connectionsCount;

    public Connections() {
        this.connectionsCount = new LongAdder();
        this.connections = new ConcurrentHashMap<>();
    }

    /*public Collection<ClientConnection> getAllConnections() {
        return connections.values();
    }*/

    public ClientConnection get(Channel channel) {
        return this.connections.get(channel.id());
    }

    public int getCount() {
        return (int) this.connectionsCount.sum(); //connections.size();
    }

    public void disconnectAll() {
        this.connections.forEach((id, conn) -> UnsafeCloseFuture.unsafeClose(conn.getChannel()));
        this.connections.clear();
        //connectionsCount does not need to be set to 0
    }

    public void addConnection(ClientConnection connection) {
        this.connections.put(connection.getChannel().id(), connection);
        this.connectionsCount.increment();

        connection.getChannel().closeFuture().addListener(future -> {
            this.removeConnection0(connection);
        });
        //Log.info("Player %s connected (%s) [%s]", connection.getUsername(), connection.getAddress(), connection.getClientVersion());
    }

    public void removeConnection0(ClientConnection connection) {
        var removed = this.connections.remove(connection.getChannel().id());
        if (removed != null) {
            this.connectionsCount.decrement();
            connection.getVerifyState().onLimboDisconnect();
            NanoLimbo.INTEGRATION.onLimboDisconnect(connection);
        }
        //Log.info("Player %s disconnected", connection.getUsername());
    }
}
