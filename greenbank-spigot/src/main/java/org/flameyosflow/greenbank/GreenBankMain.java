package org.flameyosflow.greenbank;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

import net.milkbowl.vault.economy.Economy;

public class GreenBankMain extends JavaPlugin {
    public static GreenBankMain plugin;
    public Server server = getServer();
    public PluginManager pluginManager = server.getPluginManager();
    public GreenBankMain instance;
    public Logger logger = getLogger();
    public Settings settings;
    public File file;
    public static Economy economy;
    public boolean shutdown;

    @Override
    public void onEnable() {
        plugin = this;

        file = new File(getDataFolder() + File.separator + "config.yml"); //This will get the config file
        FileConfiguration config = this.getConfig();
 
        if (!file.exists()) {
 
            //Save the default settings
            config.options().copyDefaults(true);
            saveConfig();
        }

        saveDefaultConfig(); // saves the config
        reloadConfig();  // reloads the config

        saveDefaultConfig();
        setInstance(this);

        if (!(canConnectToVault())) {
            fireCriticalError("Can't enable greenbank the economy provider due to vault not being found.", "Please download vault:");
        }
        
        logger.info("[GreenBank] GreenBank has been successfully enabled!");
    }

    public void logError(String error) {
        logger.severe(() -> error);
    }

    public void configHandler() {
        file = new File(getDataFolder() + File.separator + "config.yml"); //This will get the config file
        FileConfiguration config = this.getConfig();
 
        if (!file.exists()) {
 
            config.addDefault("Name", "Value"); //adding default settings
 
            //Save the default settings
            config.options().copyDefaults(true);
            saveConfig();
        }

        saveDefaultConfig(); // saves the config
        reloadConfig();  // reloads the config
    }

    private void fireCriticalError(String message, String error) {
        new BukkitRunnable() {
            @Override
            public void run() {
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
                shutdown = true;
                pluginManager.disablePlugin(instance);
            } 
        }.runTaskAsynchronously(plugin);
    }

    private boolean canConnectToVault() {
        return pluginManager.isPluginEnabled("Vault");
    }

    public int scheduleAsyncDelayedTask(final Runnable run) {
        return Bukkit.getScheduler().scheduleAsyncDelayedTask(this, run);
    }

    private void setInstance(GreenBankMain plugin) {
        this.instance = plugin;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        logger.info("[GreenBank] GreenBank has been successfully disabled!");
    }

    public static GreenBankMain getPlugin() {
        return plugin;
    }

    public Logger getLogger() {
        return logger;
    }

    public boolean isShutdown() {
        return shutdown;
    }
}