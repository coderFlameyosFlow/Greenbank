package org.flameyosflow.greenbank.database.sql.h2;

import com.zaxxer.hikari.HikariConfig;

import org.flameyosflow.greenbank.database.sql.SQLConnector;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The connection to the H2 database!
 *
 * @author FlameyosFlow, *insert your name/nickname here*
 */
public class H2DatabaseConnect extends SQLConnector {
    @Override
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    @Override
    public void closeConnection() throws SQLException {
        this.getConnection().close();
    }

    @Override
    public HikariConfig createConfig() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName("H2 GreenBank Database");
        hikariConfig.setMaximumPoolSize(20);

        hikariConfig.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setJdbcUrl(getConnectionURL());

        return hikariConfig;
    }

    @Override
    public String getConnectionURL() {
        return "jdbc:h2:";
    }
}
