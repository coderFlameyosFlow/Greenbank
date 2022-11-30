package org.flameyosflow.greenbank.database.sql.mysql;

// HikariCP mySQL imports.
import com.zaxxer.hikari.HikariConfig;

import java.sql.Connection;
import java.sql.SQLException;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.database.sql.SQLDatabaseConnector;

public class MySQLEngine extends SQLDatabaseConnector {
    private Connection connection;

    private GreenBankMain plugin;
    
    public MySQLEngine(String connectionURL) {
        super(null, connectionURL);
    }

    String driver = "";

    @Override
    public HikariConfig createHikariConfig() {
        // Create the connection.
        HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/greenbank");
        config.setUsername("root");
        
        return config;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // Get the connection.
        if (connection == null) {
            connection = createConnection().getConnection();
        }
        return connection;
    }

    @Override
    public void closeConnection() throws SQLException {
        // Close the connection.
        connection.close();
    }

    @Override
    public String getConnectionURL() {
        // Get the connection URL provided.
        return "jdbc:mysql://localhost:3306/greenbank";
    }
}
