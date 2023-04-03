package com.greenbank.api.database.sql.mysql;

import com.google.common.util.concurrent.AtomicDouble;
import com.greenbank.api.IGreenbank;
import com.greenbank.api.database.DatabaseConnector;
import com.greenbank.api.settings.DatabaseSettings;
import com.greenbank.api.settings.Settings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class MySQLDatabaseConnect implements DatabaseConnector {
    private Connection connection;
    private final DatabaseSettings settings;
    private final Settings configSettings;
    private final String tableName;

    public MySQLDatabaseConnect(@NotNull IGreenbank plugin) {
        this.settings = plugin.getDatabaseSettings();
        this.configSettings = plugin.getConfigSettings();
        this.tableName = settings.getTableName();
    }

    /**
     * Creates the SQL connection and makes a table in the connection if it doesn't exist.
     */
    public void createSQLConnection() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(settings.getAdvancedMySQLDriverClassName());
        config.setJdbcUrl(settings.getMySQLJdbcUrl());
        config.setUsername(settings.getMySQLUsername());
        config.setPassword(settings.getMySQLPassword());
        config.setMaximumPoolSize(settings.getMaximumPoolSize());
        config.setPoolName(settings.getPoolName());
        CompletableFuture.runAsync(() -> {
            try (HikariDataSource dataSource = new HikariDataSource(config)) {
                connection = dataSource.getConnection();
                try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid varchar(36) NOT NULL, balance double NOT NULL, bankBalance double NOT NULL, hasInfiniteMoney boolean DEFAULT FALSE);")) {
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Checks whether the player is in the database or not
     * @param playerId the player's uuid
     * @return true if the player is in the database, false otherwise
     */
    @Override
    public boolean playerNotNull(UUID playerId) {
        AtomicBoolean bool = new AtomicBoolean(false);
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM " + tableName + " WHERE uuid = ?;")) {
                statement.setString(1, playerId.toString());
                try (ResultSet set = statement.executeQuery()) {
                    bool.set(set.getString("uuid") != null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                bool.set(true);
            }
        });
        return bool.get();
    }

    @Override
    public double getMoney(UUID playerId) {
        AtomicDouble money = new AtomicDouble(0.0);
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT balance FROM " + tableName + " WHERE uuid = ?;")) {
                statement.setString(1, playerId.toString());
                try (ResultSet set = statement.executeQuery()) {
                    money.set(set.getDouble("balance"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return money.get();
    }

    @Override
    public boolean setMoney(UUID playerId, double amount) {
        AtomicBoolean moneySetSuccessfully = new AtomicBoolean(false);
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement it = connection.prepareStatement("INSERT OR REPLACE INTO account (balance) VALUES (?) WHERE uuid = ?;")) {
                it.setDouble(1, amount);
                it.setString(2, playerId.toString());
                it.executeUpdate();
                moneySetSuccessfully.set(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return moneySetSuccessfully.get();
    }

    /**
     * Adds a new player if the player is absent
     * @param playerId the player's uuid
     */
    @Override
    public void addNewPlayerIfAbsent(UUID playerId) {
        CompletableFuture.runAsync(() -> {
            if (!playerNotNull(playerId)) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO account (uuid, balance, bankBalance, hasInfiniteMoney) VALUES (?, ?, ?, ?);")) {
                    statement.setString(1, playerId.toString());
                    statement.setDouble(2, configSettings.getDefaultStartingBalance());
                    statement.setDouble(3, configSettings.getDefaultStartingBankBalance());
                    statement.setBoolean(4, false);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
