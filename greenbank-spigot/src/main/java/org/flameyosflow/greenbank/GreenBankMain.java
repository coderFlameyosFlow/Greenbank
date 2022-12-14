package org.flameyosflow.greenbank;

import java.io.IOException;

import dev.dejvokep.boostedyaml.YamlDocument;

import org.flameyosflow.greenbank.api.config.DatabaseConfig;
import org.flameyosflow.greenbank.api.config.MessagesConfig;
import org.flameyosflow.greenbank.api.config.Config;
import org.flameyosflow.greenbank.commands.*;
import org.flameyosflow.greenbank.commands.noteconomy.*;
import org.flameyosflow.greenbank.database.mongodb.MongoDBDatabaseConnect;
import org.flameyosflow.greenbank.economy.VaultEconomySupport;
import org.flameyosflow.greenbank.economy.VaultLayer;
import org.flameyosflow.greenbank.listeners.JoinListener;

/*
 * Main class of this entire plugin.
 */
public class GreenBankMain extends GreenBankSupport {
    private static GreenBankMain instance;

    private VaultEconomySupport economy;

    private MongoDBDatabaseConnect mongoConnect;

    private YamlDocument configFile;

    private YamlDocument databaseConfig;

    private YamlDocument messagesConfig;

    @Override
    public void onLoad() {
        try {
            configFile = new Config(this).configFile();
            databaseConfig = new DatabaseConfig(this).databaseConfig();
            messagesConfig = new MessagesConfig(this).messagesConfig();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        setInstance(this);

        if (!setupEconomy()) {
            fireCriticalError("Can't enable Greenbank the economy provider due to vault not being found.", "Please download vault:");
        }

        // if MongoDB exists in db.yml, use that.
        mongoConnect = new MongoDBDatabaseConnect(this);
        if (!databaseConfig.getBoolean("should-use-async-connection")) {
            mongoConnect.connect();
            getLogger().info("Synchronous connection to MongoDB database successfully complete.");
        } else {
            new Thread(() -> {
                mongoConnect.connect();
                getLogger().info("Asynchronous connection to MongoDB database successfully complete.");
            }).start();
        }

        if (configFile.getBoolean("override-all-other-plugins")) {
            registerCommand("balance", new BalanceCommand(this));
            registerCommand("pay", new PayCommand(this));
            // registerCommand("balancetop", new BalanceTopCommand());

            // Economy (Admin only).
            registerCommand("eco", new EcoCommand(this));

            // Not Economy (Admin only).
            registerCommand("greenbank", new GreenbankCommand(this));

            getLogger().info("[GreenBank] GreenBank " + getVersion() + " has been successfully enabled and did override all other economy plugins!");
        } else {
            // Don't use registerCommand as it overrides all other commands.
            getCommand("balance").setExecutor(new BalanceCommand(this));
            getCommand("pay").setExecutor(new PayCommand(this));
            // getCommand("balancetop").setExecutor(new BalanceTopCommand());

            // Economy (Admin only).
            getCommand("eco").setExecutor(new EcoCommand(this));

            // Not Economy (Admin only).
            getCommand("greenbank").setExecutor(new GreenbankCommand(this));

            getLogger().info("[GreenBank] GreenBank " + getVersion() + " has been successfully enabled, ");
            getLogger().info("Although `override-all-other-plugins` is false, ");
            getLogger().info("GreenBank will NOT override any other economy plugins or their commands/tab completes.");
        }

        registerListener(new JoinListener(this), this);
    }

    public void initConfig() throws IOException {
        databaseConfig.save();
        messagesConfig.save();
        configFile.save();

        databaseConfig.reload();
        messagesConfig.reload();
        configFile.reload();
    }

    public void logError(String error) {
        getLogger().severe(() -> error);
    }

    public void fireCriticalError(String message, String error) {
        logError("---CRITICAL ERROR!---");
        logError(" ");
        logError(message);
        logError(" ");
        logError(error + " Disabling Greenbank...");
        logError("If you think this is a bug");
        logError("or glitch please contact the");
        logError("owner or staff of this plugin.");
        logError("---------------------");

        // Do not save players or money, just shutdown
        getServer().getPluginManager().disablePlugin(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        economy = new VaultEconomySupport(this);
        VaultLayer vaultHook = new VaultLayer(this);
        vaultHook.hook();

        getLogger().info("Economy initialized and registered successfully.");
        return true;
    }

    private void setInstance(GreenBankMain plugin) {
        instance = plugin;
    }

    @Override
    public void onDisable() {
        try {
            initConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getLogger().info("[GreenBank] GreenBank has been successfully disabled!");
    }

    public static GreenBankMain getPlugin() {
        return instance;
    }

    public YamlDocument getConfigFile() {
        return configFile;
    }

    public YamlDocument getDatabaseConfigFile() {
        return databaseConfig;
    }

    public YamlDocument getMessagesConfigFile() {
        return messagesConfig;
    }

    public VaultEconomySupport getEconomy() {
        return economy;
    }

    public MongoDBDatabaseConnect getMongoConnect() {
        return mongoConnect;
    }
}