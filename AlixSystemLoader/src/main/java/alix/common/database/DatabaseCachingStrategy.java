package alix.common.database;

import alix.common.AlixCommonMain;
import alix.common.data.file.UserFileManager;
import alix.common.database.file.DatabaseConfig;

import java.util.Arrays;

public enum DatabaseCachingStrategy {

    PRELOAD,
    ON_CONNECTION_IF_EXISTS,
    ON_CONNECTION;

    public boolean requestData(String name) {
        return switch (STRATEGY) {
            case PRELOAD -> false;
            case ON_CONNECTION_IF_EXISTS -> UserFileManager.hasName(name);
            case ON_CONNECTION -> true;
        };
    }

    public static final DatabaseCachingStrategy STRATEGY = strategy0();

    private static DatabaseCachingStrategy strategy0() {
        if (!DatabaseUpdater.INSTANCE.isImpl()) return PRELOAD;

        var config = DatabaseConfig.EXTERNAL;
        String str = config.getString("caching-strategy");
        try {
            return DatabaseCachingStrategy.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            AlixCommonMain.logWarning("Invalid caching-strategy in database.yml! Available values: " + Arrays.toString(values()) + ", but got '" + str
                                      + "'! Using preload, as default!");
            return PRELOAD;
        }
    }
}