package com.greenbank.api.settings;

import com.greenbank.api.IGreenbank;
import dev.dejvokep.boostedyaml.YamlDocument;

public class DatabaseSettings {
    private final YamlDocument configFile;

    public DatabaseSettings(IGreenbank plugin) {
        this.configFile = plugin.getConfigFile();
    }

    public String getSelectedDatabase() {
        return configFile.getString("database.general-database.use-database");
    }

    public boolean shouldUseSSL() {
        return configFile.getBoolean("database.general-database.use-ssl");
    }


    public int getMaximumPoolSize() {
        return configFile.getInt("database.general-sql.maximum-pool-size");
    }

    public String getMySQLPassword() {
        return configFile.getString("database.mysql.password");
    }

    public String getMySQLUsername() {
        return configFile.getString("database.mysql.username");
    }

    public String getMySQLJdbcUrl() {
        return configFile.getString("database.mysql.jdbc-url");
    }

    public String getPoolName() {
        return configFile.getString("database.general-sql.pool-name");
    }

    public String getAdvancedMySQLDriverClassName() {
        return configFile.getString("database.mysql.advanced.driver-name");
    }

    public String getAdvancedH2DriverClassName() {
        return configFile.getString("database.H2.advanced.driver-name");
    }

    public String getH2JdbcUrl() {
        return configFile.getString("database.H2.jdbc-url");
    }

    public String getH2Password() {
        return configFile.getString("database.H2.password");
    }

    public String getH2Username() {
        return configFile.getString("database.H2.username");
    }

    public String getTableName() {
        return configFile.getString("database.general-sql.sql-table");
    }
}
