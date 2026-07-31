package alix.common.database.connect;

import alix.common.AlixCommonMain;
import alix.common.database.ThrowableConsumer;
import alix.common.database.ThrowableFunction;
import alix.common.utils.other.throwable.AlixException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.SQLTransientConnectionException;

abstract class AbstractDatabaseConnector implements DatabaseConnector {

    final HikariConfig hikariConfig = new HikariConfig();
    volatile HikariDataSource dataSource;
    private volatile boolean connected;

    @Override
    public <V> V getQuery(ThrowableFunction<V, Connection, Exception> function) throws IllegalStateException {
        try (var connection = this.obtainInterface()) {
            return function.apply(connection);
        } catch (SQLTransientConnectionException e) {
            throw new AlixException(e, "Lost connection to the database");
        } catch (Exception e) {
            throw new AlixException(e);
        }
    }

    @Override
    public void query(ThrowableConsumer<Connection, Exception> function) {
        this.getQuery(connection -> {
            function.apply(connection);
            return null;
        });
    }

    /*private static final Map<Class<? extends DatabaseConnector>, DatabaseConnector> CONNECTORS = new ConcurrentHashMap<>(4, 1.0f);

    @SneakyThrows
    private static <T extends DatabaseConnector> T newInstance(Class<T> clazz) {
        return clazz.getDeclaredConstructor().newInstance();
    }

    static <T extends DatabaseConnector> Supplier<DatabaseConnector> supply(Class<T> clazz, Supplier<T> supplier) {
        return () -> CONNECTORS.computeIfAbsent(clazz, supplier);
    }*/

    void finishConfigSetUp() {
        int cpuCores = Runtime.getRuntime().availableProcessors();
        int poolSize = Math.max(10, (cpuCores * 2) + 2);

        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setMinimumIdle(poolSize); // Fixed-size pool for steady performance

        hikariConfig.setConnectionTimeout(20_000); // 20 seconds
        hikariConfig.setValidationTimeout(3_000);    // 3 seconds

        hikariConfig.setLeakDetectionThreshold(15_000);
    }

    @SneakyThrows
    @Override
    public void connect() {
        this.finishConfigSetUp();

        dataSource = new HikariDataSource(hikariConfig);
        connected = true;
        obtainInterface().close(); //Verify connection
        AlixCommonMain.logInfo("Successfully connected to the database");
    }

    @Override
    public void disconnect() {
        connected = false;
        dataSource.close();
    }

    @SneakyThrows
    @Override
    public Connection obtainInterface() {
        if (!connected) throw new AlixException("Not connected to the database!");
        return dataSource.getConnection();
    }
}