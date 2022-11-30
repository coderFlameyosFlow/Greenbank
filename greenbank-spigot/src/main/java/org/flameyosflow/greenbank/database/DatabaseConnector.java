package org.flameyosflow.greenbank.database;

import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

public interface DatabaseConnector {

    /**
     * Creates a new connection to the database
     *
     * @param type of class
     * @return A new connection to the database using the settings provided
     */
    HikariDataSource createConnection();

    /**
     * Close the database connection
     * @throws SQLException
     */
    void closeConnection() throws SQLException;

    /**
     * Returns the connection url
     *
     * @return the connector's URL
     */
    String getConnectionURL();


}
