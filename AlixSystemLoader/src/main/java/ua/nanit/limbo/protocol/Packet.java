package ua.nanit.limbo.protocol;

import alix.common.utils.other.throwable.AlixError;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ua.nanit.limbo.connection.ClientConnection;
import ua.nanit.limbo.protocol.registry.Version;
import ua.nanit.limbo.server.LimboServer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Packet {

    void encode(ByteMessage msg, Version version);

    void decode(ByteMessage msg, Version version);

    @Skip
    default void handle(ClientConnection conn, LimboServer server) {
        // Ignored by default
    }

    default boolean isSkippable(ClientConnection conn) {
        return false;
    }

    default PacketWrapper<?> packetWrapper(Version version) {
        throw new AlixError("packetWrapper(...) is unsupported on " + this.getClass().getSimpleName());
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Skip {
    }
}
