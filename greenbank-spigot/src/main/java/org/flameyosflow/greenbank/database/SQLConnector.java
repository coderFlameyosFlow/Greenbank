package org.flameyosflow.greenbank.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

/**
 * This class is perfectly setup to work with HikariCP,
 * because we expect people to want SQL.
 *
 * @author FlameyosFlow
 */
public interface SQLConnector {

    /**
     * Creates a new connection to the database
     *
     * @return A new connection to the database using the settings provided
     */
    HikariDataSource createConnection();

    /**
     * Creates a new connection to the database
     *
     * @return The connection to the database.
     */
    Connection getConnection();

    /**
     * Close the database connection
     */
    void closeConnection() throws SQLException;

    /**
     * Returns the connection url
     *
     * @return the connector's URL
     */
    default String getConnectionURL() {
        return "";
    }
}
