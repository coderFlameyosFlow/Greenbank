package org.flameyosflow.greenbank.database.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.database.DatabaseConnector;

import com.zaxxer.hikari.HikariConfig;
// HikariCP imports
import com.zaxxer.hikari.HikariDataSource;

public abstract class SQLDatabaseConnector implements DatabaseConnector {
    public HikariDataSource dataSource;

    public GreenBankMain plugin;

    public SQLDatabaseConnector(Object object, String connectionURL) {
    }

    public abstract HikariConfig createHikariConfig();

    /*
     * creates a new connection.
     */
    @Override
    public HikariDataSource createConnection() {
        // Only make one connection to the database
        if (dataSource == null) {
            try {
                dataSource = new HikariDataSource(this.createHikariConfig());

                // Test connection
                try (Connection connection = dataSource.getConnection()) {
                    connection.isValid(5 * 1000);
                }
            } catch (SQLException e)  {
                plugin.logger.severe("Could not connect to the database! " + e.getMessage());
                dataSource = null;
            }
        }

        return dataSource;
    }

    /*
     * returns the current connection
     */
    public abstract Connection getConnection() throws SQLException;
}
