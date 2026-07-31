package ua.nanit.limbo.server;

import ua.nanit.limbo.NanoLimbo;
import ua.nanit.limbo.configuration.LimboConfig;
import ua.nanit.limbo.connection.ClientChannelInitializer;
import ua.nanit.limbo.connection.ClientConnection;
import ua.nanit.limbo.connection.PacketHandler;
import ua.nanit.limbo.connection.captcha.CaptchaBlock;
import ua.nanit.limbo.connection.pipeline.compression.CompressionHandler;
import ua.nanit.limbo.integration.LimboIntegration;
import ua.nanit.limbo.protocol.snapshot.PacketSnapshots;
import ua.nanit.limbo.world.DimensionRegistry;

import java.io.IOException;

public final class LimboServer {

    private final LimboIntegration<ClientConnection> integration;
    private final ClientChannelInitializer clientChannelInitializer;
    private final LimboConfig config;
    private final PacketHandler packetHandler;
    //private final CommandHandler commandHandler;
    private final Connections connections;
    private final DimensionRegistry dimensionRegistry;
    //private final ScheduledFuture<?> keepAliveTask;

    public LimboServer(LimboIntegration<ClientConnection> integration) throws IOException {
        NanoLimbo.INTEGRATION = integration;
        NanoLimbo.LIMBO = this;

        Log.info("Starting virtual server...");
        this.integration = integration;
        this.config = new LimboConfig();

        //ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

        //this.commandHandler = integration.createCommandHandler();
        this.dimensionRegistry = new DimensionRegistry();
        this.connections = new Connections();

        PacketSnapshots.init();
        this.packetHandler = new PacketHandler(this);

        //keepAliveTask = null;//serverChannel.eventLoop().parent().scheduleAtFixedRate(this::broadcastKeepAlive, 0L, 5L, TimeUnit.SECONDS);

        //Runtime.getRuntime().addShutdownHook(new Thread(this::onDisable, "NanoLimbo shutdown thread"));

        this.clientChannelInitializer = new ClientChannelInitializer(this);
        //UiiaiuiiiaiCat.init();
        CaptchaBlock.init();
        Log.info("Server started.");
    }

    public LimboIntegration<ClientConnection> getIntegration() {
        return integration;
    }

    public LimboConfig getConfig() {
        return config;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    /*public CommandHandler getCommandHandler() {
        return commandHandler;
    }*/

    public Connections getConnections() {
        return connections;
    }

    public DimensionRegistry getDimensionRegistry() {
        return dimensionRegistry;
    }

    public ClientChannelInitializer getClientChannelInitializer() {
        return clientChannelInitializer;
    }

    /*private void broadcastKeepAlive() {
        connections.getAllConnections().forEach(ClientConnection::sendKeepAlive);
    }*/

    public void onDisable() {
        Log.info("Stopping server...");

        this.connections.disconnectAll();
        CompressionHandler.releaseAll();
        PacketSnapshots.releaseAll();
        //if (keepAliveTask != null) keepAliveTask.cancel(true);

        Log.info("Server stopped.");
    }
}
