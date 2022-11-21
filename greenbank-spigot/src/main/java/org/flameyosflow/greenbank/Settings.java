package org.flameyosflow.greenbank;

import java.util.HashMap;
import java.util.Map;

import org.flameyosflow.greenbank.config.*;
import org.flameyosflow.greenbank.database.DatabaseType;

@StoreAt(filename="sql.yml") // Explicitly call out what name this should have.
public class Settings implements ConfigObject {

    // General
    //@ConfigComment("Override economy or not, if this is true (which it should be since it's the whole point of this plugin).")
    //@ConfigComment("Then it will override all other economy plugins, if false then it will not.")
    //@ConfigComment("You will only want this for the specified reasons:")
    //@ConfigComment("    You are still not ready to migrate to this plugin,")
    //@ConfigComment("    And other valid reasons.")
    //@ConfigComment("Please note this will disable the plugin.")
    //@ConfigComment("If there is no other economy plugin present anyway, this will be ignored.")
    //@ConfigEntry(path = "general.override-other-economy-plugins")
    //private boolean overrideOtherEconomyPlugins = true;

    // TODO The code above me will soon be migrated to config.yml

    // Database
    @ConfigComment("Avaliable Databases: MYSQL and SQLITE only.")
    @ConfigComment("If you need others, please make a feature request, or pull request to github (this is much appreciated.).")
    @ConfigComment("Minimum required versions:")
    @ConfigComment("   MySQL versions 5.7 or later")
    @ConfigComment("   SQLite versions 3.28 or later")
    @ConfigComment("Greenbank uses HikariCP for connecting with SQL databases.")
    @ConfigComment("For performance, stability and readability reasons.")
    @ConfigEntry(path = "general.database.type")
    private DatabaseType databaseType = DatabaseType.MYSQL;

    @ConfigComment("The host of the MySQL/other database.")
    @ConfigEntry(path = "general.database.host")
    private String databaseHost = "localhost";

    @ConfigComment("Port 3306 is MySQL's default.")
    @ConfigEntry(path = "general.database.port")
    private int databasePort = 3306;

    @ConfigComment("The name of the MySQL/other database.")
    @ConfigEntry(path = "general.database.name")
    private String databaseName = "root";

    @ConfigComment("The username of the MySQL/other database.")
    @ConfigEntry(path = "general.database.username")
    private String databaseUsername = "root";

    @ConfigComment("The password of the MySQL/other database.")
    @ConfigEntry(path = "general.database.password")
    private String databasePassword = "root";

    @ConfigComment("How often the data will be saved to file in mins. Default is 5 minutes.")
    @ConfigComment("This helps prevent issues if the server crashes.")
    @ConfigComment("Data is also saved at important points in the game.")
    @ConfigEntry(path = "general.database.backup-period")
    private int databaseBackupPeriod = 5;

    @ConfigComment("How many players will be saved in one tick. Default is 200")
    @ConfigComment("Reduce if you experience lag while saving.")
    @ConfigComment("Do not set this too low or data might get lost!")
    @ConfigEntry(path = "general.database.max-saved-players-per-tick")
    private int maxSavedPlayersPerTick = 20;

    @ConfigComment("Number of active connections to the SQL database at the same time.")
    @ConfigComment("Default 10.")
    @ConfigEntry(path = "general.database.max-pool-size")
    private int maximumPoolSize = 10;

    @ConfigComment("Enable SSL connection to MongoDB and MySQL databases.")
    @ConfigEntry(path = "general.database.use-ssl")
    private boolean useSSL = false;

    @ConfigComment("Database table prefix. Adds a prefix to the database tables. Not used by flatfile databases.")
    @ConfigComment("Only the characters A-Z, a-z, 0-9 can be used. Invalid characters will become an underscore.")
    @ConfigComment("Set this to a unique value if you are running multiple BentoBox instances that share a database.")
    @ConfigComment("Be careful about length - databases usually have a limit of 63 characters for table lengths")
    @ConfigEntry(path = "general.database.prefix-character")
    private String databasePrefix = "";

    @ConfigComment("Custom connection datasource properties that will be applied to connection pool.")
    @ConfigComment("Check available values to your SQL driver implementation.")
    @ConfigComment("Example: ")
    @ConfigComment("  custom-properties: ")
    @ConfigComment("    cachePrepStmts: 'true'")
    @ConfigComment("    prepStmtCacheSize: '250'")
    @ConfigComment("    prepStmtCacheSqlLimit: '2048'")
    @ConfigEntry(path = "general.database.custom-properties")
    private Map<String, String> customPoolProperties = new HashMap<>();

    /**
     * @return the databasePrefix
     */
    public String getDatabasePrefix() {
        if (databasePrefix == null) databasePrefix = "";
        return databasePrefix.isEmpty() ? "" : databasePrefix.replaceAll("[^a-zA-Z0-9]", "_");
    }

    /**
     * @param databasePrefix the databasePrefix to set
     */
    public void setDatabasePrefix(String databasePrefix) {
        this.databasePrefix = databasePrefix;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    /**
     * This method returns the useSSL value.
     * @return the value of useSSL.
     */
    public boolean isUseSSL() {
        return useSSL;
    }

    /**
     * This method sets the useSSL value.
     * @param useSSL the useSSL new value.
     */
    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public int getDatabaseBackupPeriod() {
        return databaseBackupPeriod;
    }

    public void setDatabaseBackupPeriod(int databaseBackupPeriod) {
        this.databaseBackupPeriod = databaseBackupPeriod;
    }

    public int getMaxSavedPlayersPerTick() {
        return maxSavedPlayersPerTick;
    }

    public void setMaxSavedPlayersPerTick(int maxSavedPlayersPerTick) {
        this.maxSavedPlayersPerTick = maxSavedPlayersPerTick;
    }

    public int getMaximumPoolSize()
    {
        return maximumPoolSize;
    }
}
