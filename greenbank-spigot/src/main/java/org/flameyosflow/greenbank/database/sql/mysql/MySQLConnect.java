package org.flameyosflow.greenbank.database.sql.mysql;

import com.zaxxer.hikari.HikariConfig;

import org.flameyosflow.greenbank.database.sql.SQLConnect;
import org.flameyosflow.greenbank.database.DatabaseConnectionSettingsImpl;
import org.jetbrains.annotations.NotNull;

public class MySQLConnect extends SQLConnect {
    private String MySQLName = "root";
    private String MySQLPass = "";

    //Database URL, Port and JDBC Driver
    private String MySQLUrl = "root";
    private String MySQLPort = "3306";
    private String MySQLDatabaseName = "root";
    private String MySQLDriver = "com.mysql.cj.jdbc.Driver";

    public MySQLConnect(@NotNull DatabaseConnectionSettingsImpl dbSettings) {
        super(dbSettings, url(dbSettings));
    }

    public static String url(DatabaseConnectionSettingsImpl dbSettings) {
        String url = "dbc:mysql://" + dbSettings.getHost() + ":" + dbSettings.getPort() + "/" + dbSettings.getDatabaseName();
        return url;
    }


    @Override
    public HikariConfig createConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", MySQLUrl, MySQLPort, MySQLDatabaseName));
        config.setUsername(MySQLName);
        config.setPassword(MySQLPass);  
        config.setDriverClassName(MySQLDriver);

        config.addDataSourceProperty("useSSL", "false");

        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("encoding", "UTF-8");
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("allowMultiQueries", "true");

        config.addDataSourceProperty("allowPublicKeyRetrieval", "true");
    
        return config;
    }
}
