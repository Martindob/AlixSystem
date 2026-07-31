package alix.common.utils.netty.safety;

import ua.nanit.limbo.NanoLimbo;

public final class NettySafetyException extends RuntimeException {

    NettySafetyException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {//traceless
        return NanoLimbo.broadcastInvalidPacketFireWalls ? super.fillInStackTrace() : this;
    }

    @Override
    public Throwable getCause() {
        return NanoLimbo.broadcastInvalidPacketFireWalls ? super.getCause() : null;
    }

    static NettySafetyException of(String reason) {
        return new NettySafetyException(reason);
    }
}