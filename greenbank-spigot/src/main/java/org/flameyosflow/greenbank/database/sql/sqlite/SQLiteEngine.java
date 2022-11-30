package org.flameyosflow.greenbank.database.sql.sqlite;

// HikariCP imports
import com.zaxxer.hikari.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.flameyosflow.greenbank.database.sql.SQLDatabaseConnector;
   

/**
 * HikariCP
 *
 * @author flameyosflow
 */

public class SQLiteEngine extends SQLDatabaseConnector {
    // Driver of this engine.
    private static final String DRIVER_NAME = "org.sqlite.JDBC";

    public Connection connection;

    public SQLiteEngine(String connectionURL) {
        super(null, connectionURL);
        this.connection = (Connection) connection;
    }

    @Override
    public HikariConfig createHikariConfig() {
        // Create the actual connection.
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.sqlite.SQLiteDataSource");
        config.setPoolName("Greenbank-SQLite-Pool");

        config.setDriverClassName(DRIVER_NAME);
        config.setJdbcUrl("jdbc:sqlite:plugins/GreenBank/" + "" + ".db");
        config.setMaximumPoolSize(50); // 50 Connections (including idle connections)
        
        return config;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // return connection
        return createConnection().getConnection();
    }


    @Override
    public void closeConnection() throws SQLException {
        // Get the connection then close it
        if (getConnection() != null && !getConnection().isClosed()) {
            try {
                getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getConnectionURL() {
        // Get the connection URL 
        return "jdbc:sqlite:plugins/GreenBank/" + "" + ".db";
    }
}