package org.flameyosflow.greenbank;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import dev.dejvokep.boostedyaml.YamlDocument;

import org.bukkit.scheduler.BukkitRunnable;

import org.flameyosflow.greenbank.api.config.DatabaseConfig;
import org.flameyosflow.greenbank.api.config.MessagesConfig;
import org.flameyosflow.greenbank.api.config.Config;
import org.flameyosflow.greenbank.commands.*;
import org.flameyosflow.greenbank.commands.noteconomy.*;
import org.flameyosflow.greenbank.database.mongodb.MongoDBDatabaseConnect;
import org.flameyosflow.greenbank.economy.VaultEconomySupport;
import org.flameyosflow.greenbank.economy.VaultLayer;
import org.flameyosflow.greenbank.listeners.JoinListener;

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
        BukkitRunnable connectDatabaseAsync = new BukkitRunnable() {
            @Override
            public void run() {
                mongoConnect.connect();
            }
        };

        if (configFile.getBoolean("should-use-async-connection")) {
            connectDatabaseAsync.runTaskAsynchronously(this);
        } else {
            connectDatabaseAsync.runTask(this);
        }

        getLogger().info(" ");
        getLogger().info("Synchronous connection to MongoDB database successfully complete.");

        registerCommand("balance", new BalanceCommand(this));
        registerCommand("pay", new PayCommand(this));
        registerCommand("eco", new EcoCommand(this));
        registerCommand("greenbank", new GreenbankCommand(this));

        registerListener(new JoinListener(this), this);
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

    public void initConfig() throws IOException {
        databaseConfig.save();
        messagesConfig.save();
        configFile.save();

        databaseConfig.reload();
        messagesConfig.reload();
        configFile.reload();
        getLogger().info("Successfully saved and reloaded ALL configuration files.");
    }

    public void logError(String error) {
        getLogger().severe(() -> error);
    }

    public InputStream getConfigYaml(String fileName) {
        try {
            return Objects.requireNonNull(getResource(fileName));
        } catch (NullPointerException error) {
            error.printStackTrace();
            return null;
        }
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
