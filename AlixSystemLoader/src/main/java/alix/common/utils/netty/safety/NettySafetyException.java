package alix.common.utils.netty.safety;

import ua.nanit.limbo.NanoLimbo;

public final class NettySafetyException extends RuntimeException {

    NettySafetyException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {//traceless
        return NanoLimbo.broadcastInvalidPacketFireWallStackTraces ? super.fillInStackTrace() : this;
    }

    @Override
    public Throwable getCause() {
        return NanoLimbo.broadcastInvalidPacketFireWallStackTraces ? super.getCause() : null;
    }

    static NettySafetyException of(String reason) {
        return new NettySafetyException(reason);
    }
}