package org.flameyosflow.greenbank.database;

import org.jetbrains.annotations.NotNull;

public interface DatabaseConnector {

    /**
     * Creates a new connection to the database
     *
     * @param type of class
     * @return A new connection to the database using the settings provided
     */
    Object createConnection(Class<?> type);

    /**
     * Close the database connection
     * @param type of class being closed
     */
    void closeConnection(Class<?> type);

    /**
     * Returns the connection url
     *
     * @return the connector's URL
     */
    String getConnectionUrl();

    /**
     * Looks through multiple files and returns a unique key
     *
     * @param tableName - name of the table
     * @return a unique key for this record
     */
    @NotNull
    String getUniqueId(String tableName);

    /**
     * Check if the key exists in the database in this table or not
     *
     * @param tableName - name of the table
     * @param key       - key to check
     * @return true if it exists
     */
    boolean uniqueIdExists(String tableName, String key);
}
