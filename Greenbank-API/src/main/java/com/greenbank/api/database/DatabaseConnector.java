package com.greenbank.api.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseConnector {
    boolean setMoney(UUID playerId, double amount) throws SQLException;

    void addNewPlayerIfAbsent(UUID playerId);

    boolean playerNotNull(UUID playerId);

    double getMoney(UUID playerId);

    void createSQLConnection();

    Connection getConnection();

    boolean isConnected();

    void close() throws SQLException;
}
