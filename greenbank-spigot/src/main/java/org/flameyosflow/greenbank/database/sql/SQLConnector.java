package org.flameyosflow.greenbank.database.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import org.flameyosflow.greenbank.database.DatabaseConnector;

public abstract class SQLConnector implements DatabaseConnector {
    private HikariDataSource dataSource;

    /**
     * Creates a new config used to create the actual connection.
     *
     * @return the hikariConfig variable.
     */
    public abstract HikariConfig createConfig();

    @Override
    public HikariDataSource createConnection() {
        // if dataSource already exists, do nothing and just return the dataSource.
        if (!(dataSource == null)) {
            return dataSource;
        }

        // This code is executed if the dataSource is actually null, and WE DO NOT WANT THAT!
        try {
            setDataSource(new HikariDataSource(createConfig()));

            // Test connection
            try (Connection connection = this.getConnection()) {
                connection.isValid(5 * 1000);
            }
        } catch (SQLException e)  {
            Bukkit.getLogger().severe("Could not connect to the database! " + e.getMessage());
            e.printStackTrace();
            dataSource = null;
        }

        return dataSource;
    }

    protected HikariDataSource getDataSource() {
        return dataSource;
    }

    protected void setDataSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
