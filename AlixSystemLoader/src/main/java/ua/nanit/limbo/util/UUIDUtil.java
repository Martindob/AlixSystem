package ua.nanit.limbo.util;

import alix.common.utils.other.annotation.OptimizationCandidate;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class UUIDUtil {

    private UUIDUtil() {}

    //For both, Velocity & Spigot forks
    //https://github.com/PaperMC/Velocity/blob/1edab1411d3afb95cb6f5c7c8227fc41f1260506/api/src/main/java/com/velocitypowered/api/util/UuidUtils.java#L52
    public static UUID getOfflineModeUuid(String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username)
                .getBytes(StandardCharsets.UTF_8));
    }

    //From NanoLimbo UuidUtil
    //Possible optimization: https://github.com/jonesdevelopment/sonar/blob/main/common/src/main/java/xyz/jonesdev/sonar/common/util/FastUuidSansHyphens.java
    @OptimizationCandidate
    public static UUID fromString(String str) {
        if (str.contains("-"))
            return UUID.fromString(str);
        return UUID.fromString(str.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
    }
}
