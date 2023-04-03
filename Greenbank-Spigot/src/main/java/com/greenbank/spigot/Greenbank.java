package com.greenbank.spigot;

import com.greenbank.api.IGreenbank;
import com.greenbank.api.IPlugin;
import com.greenbank.api.config.Config;
import com.greenbank.api.database.DatabaseHandler;
import com.greenbank.spigot.commands.*;
import com.greenbank.spigot.hooks.vault.VaultEconomySupport;
import com.greenbank.api.settings.DatabaseSettings;
import com.greenbank.api.settings.MessagesSettings;
import com.greenbank.api.settings.Settings;

import dev.dejvokep.boostedyaml.YamlDocument;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Greenbank extends JavaPlugin implements IGreenbank {
    private YamlDocument config;
    private Settings configSettings;
    private DatabaseSettings databaseSettings;
    private MessagesSettings messagesSettings;
    private VaultEconomySupport economy;
    private DatabaseHandler databaseHandler;
    private PluginManager manager;

    @Override
    public void onEnable() {
        Server server = getServer();
        createConfig();
        configSettings = new Settings(this);
        databaseSettings = new DatabaseSettings(this);
        messagesSettings = new MessagesSettings(this);

        manager = server.getPluginManager();
        if (manager.getPlugin("Vault") == null) {
            getLogger().severe("Vault not Found, disabling plugin immediately.");
            manager.disablePlugin(this);
            return;
        }
        try {
            databaseHandler = new DatabaseHandler(this);
            databaseHandler.createSQLConnection();
            if (!databaseHandler.isConnected()) {
                getLogger().severe("Invalid credentials/database type, CANNOT ENABLE PLUGIN, DISABLING...");
                manager.disablePlugin(this);
            }
        } catch (NullPointerException error) {
            // This is usually thrown when the database returns null
            // and a lot of the times, it fails making the config for some really weird reason, it is never created when first time enabling the plugin and then continues not creating it,
            // so we try creating it again just in case
            createConfig();
            manager.disablePlugin(this);
        }

        economy = new VaultEconomySupport(this);
        server.getServicesManager().register(Economy.class, economy, this, ServicePriority.Normal);

        BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.registerDependency(Economy.class, this.getEconomy());
        handler.registerDependency(MessagesSettings.class, this.getMessagesSettings());
        handler.registerDependency(DatabaseSettings.class, this.getDatabaseSettings());
        handler.registerDependency(Settings.class, this.getConfigSettings());
        handler.register(new BalanceCommand(),
                new CommandGreenbank(),
                new EcoCommand(),
                new PayCommand(),
                new ConvertEconomyCommand());
        handler.registerBrigadier();

        new IPlugin(this);
        getLogger().info("Successfully enabled Greenbank");
    }

    @Override
    public void onDisable() {
        try {
            databaseHandler.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disablePlugin() {
        manager.disablePlugin(this);
    }

    @Override
    public YamlDocument getConfigFile() {
        return config;
    }

    @Override
    public Settings getConfigSettings() {
        return configSettings;
    }

    @Override
    public DatabaseSettings getDatabaseSettings() {
        return databaseSettings;
    }

    @Override
    public MessagesSettings getMessagesSettings() {
        return messagesSettings;
    }

    @Override
    public VaultEconomySupport getEconomy() {
        return economy;
    }

    @Override
    public DatabaseHandler getDatabaseConnect() {
        return databaseHandler;
    }

    private void createConfig() {
        try {
            config = Config.configFile(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}