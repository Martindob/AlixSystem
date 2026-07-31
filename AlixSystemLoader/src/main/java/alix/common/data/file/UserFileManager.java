package alix.common.data.file;

import alix.common.AlixCommonMain;
import alix.common.connection.filters.GeoIPTracker;
import alix.common.connection.filters.PlayerNameIndex;
import alix.common.data.PersistentUserData;
import alix.common.database.DatabaseUpdater;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UserFileManager {

    private static final Map<String, PersistentUserData> map = new ConcurrentHashMap<>();
    private static final UserFile file = new UserFile();
    private static final DatabaseUpdater database = DatabaseUpdater.INSTANCE;

    static {
        database.createTablesSync();
        try {
            file.load();//read the file

            var local = new HashSet<>(map.values());

            //supplementary data with database
            database.loadAllUsers(map).thenRun(() -> {
                file.save(map);//save all to local cache

                //local-only + db-only = all
                //local-only = all - db-only
                var dbOnly = new HashSet<>(map.values());
                dbOnly.removeAll(local);

                var localOnly = new HashSet<>(map.values());
                localOnly.removeAll(dbOnly);

                if (dbOnly.isEmpty()) return;

                String plural = localOnly.size() != 1 ? "s" : "";
                AlixCommonMain.logInfo("Saving " + localOnly.size() + " new user" + plural + " to the database");
                save(localOnly);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onAsyncSave() {
        save0();

        AlixCommonMain.debug("Successfully saved users.yml file!");
    }

    public static void fastSave() {
        save0();
    }

    static void save0() {
        file.save(map);
        //getAllData().stream().filter(data -> data.isDirty).forEach(PersistentUserData::saveToDatabase);
    }

    public static PersistentUserData get(String name) {
        return name != null ? map.get(name) : null;
    }

    public static PersistentUserData remove(String name) {
        //do not remove from UserTokensFileManager
        PlayerNameIndex.remove(name);
        var data = map.remove(name);
        if (data != null) {
            GeoIPTracker.removeIP(data.getSavedIP());
            database.removeByName(name);
        }
        return data;
    }

    public static boolean hasName(String name) {
        return get(name) != null;
    }

    public static void putData(PersistentUserData data) {
        map.put(data.getName(), data);
        PlayerNameIndex.index(data.getName());
        //data.saveToDatabase();
    }

    public static Collection<PersistentUserData> getAllData() {
        return map.values();
    }

    public static void init() {
    }

    static void save(Collection<PersistentUserData> list) {
        list.forEach(PersistentUserData::saveToDatabase);
    }

    @SneakyThrows
    public static void saveLocalToDb() {
        if (!database.isImpl()) return;
        save(getAllData());
    }
}