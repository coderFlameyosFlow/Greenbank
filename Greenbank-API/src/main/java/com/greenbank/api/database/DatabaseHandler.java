package com.greenbank.api.database;

import com.greenbank.api.IGreenbank;
import com.greenbank.api.database.sql.h2.H2DatabaseConnect;
import com.greenbank.api.database.sql.mysql.MySQLDatabaseConnect;
import com.greenbank.api.settings.DatabaseSettings;

import java.sql.SQLException;
import java.util.UUID;

public class DatabaseHandler {
    private final Database database;
    private final boolean equalsH2;
    private final boolean equalsMySQL;
    private final H2DatabaseConnect h2;
    private final MySQLDatabaseConnect mysql;

    public DatabaseHandler(IGreenbank plugin) {
        this.database = new Database(plugin);
        DatabaseSettings settings = plugin.getDatabaseSettings();
        String selectedDatabase = settings.getSelectedDatabase().toLowerCase();
        this.equalsH2 = selectedDatabase.equals("h2");
        this.equalsMySQL = selectedDatabase.equals("mysql");
        this.h2 = new H2DatabaseConnect(plugin);
        this.mysql = new MySQLDatabaseConnect(plugin);
    }

    /**
     * Create a new Connection to the database.
     *
     * @author FlameyosFlow
     * @since 1.2.1-alpha
     */
    public void createSQLConnection() {
        if (equalsH2) {
            database.createSQLConnection(h2);
        } else if (equalsMySQL) {
            database.createSQLConnection(mysql);
        }
    }

    /**
     * Closes the Connection to the database.
     *
     * @author FlameyosFlow
     * @since 1.2.1-alpha
     */
    public void close() throws SQLException {
        if (equalsH2) {
            database.close(h2);
        } else if (equalsMySQL) {
            database.close(mysql);
        }
    }

    /**
     * Checks if the plugin successfully connected to the economy.
     *
     * @author FlameyosFlow
     * @since 1.2.1-alpha
     * @return true if the database is connected
     */
    public boolean isConnected() {
        if (equalsH2) {
            return database.isConnected(h2);
        } else if (equalsMySQL) {
            return database.isConnected(mysql);
        } else {
            return false;
        }
    }

    /**
     * Gets the balance of the player.
     *
     * @author FlameyosFlow
     * @since 1.0.0-alpha
     * @return the balance of the player
     */
    public double getMoney(UUID playerId) {
        if (equalsH2) return database.getMoney(playerId, h2);
        else if (equalsMySQL) return database.getMoney(playerId, mysql);
        else return 0.0;
    }

    /**
     * Sets the balance of the player.
     *
     * @author FlameyosFlow
     * @since 1.0.0-alpha
     * @return the balance of the player
     */
    public boolean setMoney(UUID playerId, double amount) throws SQLException {
        if (equalsH2) return database.setMoney(playerId, amount, h2);
        else if (equalsMySQL) return database.setMoney(playerId, amount, mysql);
        else return false;
    }

    /**
     * Adds a new player to the database if they are absent
     *
     * @author FlameyosFlow
     * @since 1.0.0-alpha
     */
    public void addNewPlayerIfAbsent(UUID playerId) {
        if (equalsH2) database.addNewPlayerIfAbsent(playerId, h2);
        else if (equalsMySQL) database.addNewPlayerIfAbsent(playerId, mysql);
    }

    /**
     * Gets if the player is in the database.
     *
     * @author FlameyosFlow
     * @since 1.0.0-alpha
     * @return true if the player is not null
     */
    public boolean playerNotNull(UUID playerId) {
        return database.playerNotNull(playerId, (equalsH2 ? h2 : (equalsMySQL ? mysql : null)));
    }
    
    private static final class Database {
        private final IGreenbank plugin;

        public Database(IGreenbank plugin) {
            this.plugin = plugin;
        }

        private double getMoney(UUID playerId, DatabaseConnector databaseConnector) {
            return databaseConnector.getMoney(playerId);
        }
        
        private boolean setMoney(UUID playerId, double amount, DatabaseConnector databaseConnector) throws SQLException {
            if (databaseConnector == null) {

            }
            return databaseConnector.setMoney(playerId, amount);
        }
        
        private void addNewPlayerIfAbsent(UUID playerId, DatabaseConnector databaseConnector) {
            databaseConnector.addNewPlayerIfAbsent(playerId);
        }
        
        private boolean playerNotNull(UUID playerId, DatabaseConnector databaseConnector) {
            return databaseConnector.playerNotNull(playerId);
        }

        private void createSQLConnection(DatabaseConnector databaseConnector) {
            databaseConnector.createSQLConnection();
        }


        private void close(DatabaseConnector databaseConnector) throws SQLException {
            databaseConnector.close();
        }

        private boolean isConnected(DatabaseConnector databaseConnector) {
            return databaseConnector.isConnected();
        }
    }
}