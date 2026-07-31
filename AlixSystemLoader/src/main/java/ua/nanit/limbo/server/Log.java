package ua.nanit.limbo.server;


import ua.nanit.limbo.NanoLimbo;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private static final Logger LOGGER = Logger.getLogger("AlixVirtualLimbo");

    private Log() {
    }

    public static void info(Object msg) {
        LOGGER.info(msg.toString());
    }

    public static void info(Object msg, Object... args) {
        LOGGER.info(String.format(msg.toString(), args));
    }

    public static void debug(Object msg, Object... args) {
        LOGGER.log(Level.CONFIG, String.format(msg.toString(), args));
    }

    public static void warning(Object msg) {
        LOGGER.warning(msg.toString());
    }

    public static void warning(Object msg, Object... args) {
        LOGGER.warning(String.format(msg.toString(), args));
    }

    public static void warning(Object msg, Throwable t, Object... args) {
        LOGGER.log(Level.WARNING, String.format(msg.toString(), args), t);
    }

    public static void error(Object msg) {
        LOGGER.severe(msg.toString());
    }

    public static void error(Object msg, Object... args) {
        LOGGER.severe(String.format(msg.toString(), args));
    }

    public static void error(Object msg, Throwable t, Object... args) {
        LOGGER.log(Level.SEVERE, String.format(msg.toString(), args), t);
    }

    public static boolean isDebug() {
        return NanoLimbo.debugMode;
    }
}
