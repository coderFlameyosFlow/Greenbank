package org.flameyosflow.greenbank.database;

import java.sql.SQLException;


/**
 * This class is perfectly setup to work with HikariCP,
 * because we expect people to want SQL.
 *
 * @author FlameyosFlow
 */
public interface DatabaseConnector<T> {

    /**
     * Creates a new connection to the database
     *
     * @return A new connection to the database using the settings provided
     */
    T createConnection();

    /**
     * Creates a new connection to the database
     *
     * @return The connection to the database.
     */
    Object getConnection() throws SQLException;

    void closeConnection();

    void edit(String uuid, Object value, String identifier);

    void delete(Object delete);
}
