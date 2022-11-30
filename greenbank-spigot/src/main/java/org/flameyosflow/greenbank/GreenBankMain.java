package org.flameyosflow.greenbank;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;

import net.milkbowl.vault.economy.Economy;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
/*
 * Main class of this entire plugin.
 */
public class GreenBankMain extends JavaPlugin {
    public static GreenBankMain plugin;
    public Server server = getServer();
    public PluginManager pluginManager = server.getPluginManager();
    public GreenBankMain instance;
    public Logger logger = getLogger();
    public File file;
    public YamlDocument config;
    public static Economy economy;
    public boolean shutdown;

    public final HashMap<UUID, Long> playerBank = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        setInstance(this);

        if (!canConnectToVault() || !canConnectToVilib()) {
            fireCriticalError("Can't enable Greenbank the economy provider due to vault and/or vilib not being found.", "Please download vault:");
        }
        
        logger.info("[GreenBank] GreenBank has been successfully enabled!");
    }

    public void logError(String error) {
        logger.severe(() -> error);
    }

    public void configHandler() {
        try {
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        } catch (IOException ex) {
            ex.printStackTrace();
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
    private boolean canConnectToVilib() {
        return pluginManager.isPluginEnabled("vilib");
    }

    @SuppressWarnings("deprecation")
    public int scheduleAsyncDelayedTask(Runnable run) {
        // Suppressed deprecation because it's useless to deprecate an async task.
        return Bukkit.getScheduler().scheduleAsyncDelayedTask(this, run);
    }

    private void setInstance(GreenBankMain plugin) {
        this.instance = plugin;
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        logger.info("[GreenBank] GreenBank has been successfully disabled!");
    }

    public static GreenBankMain getPlugin() {
        return plugin;
    }
    public boolean isShutdown() {
        return shutdown;
    }
}