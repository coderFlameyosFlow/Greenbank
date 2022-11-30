package org.flameyosflow.greenbank.api.config;

import java.util.logging.Logger;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.flameyosflow.greenbank.GreenBankMain;

import dev.efnilite.vilib.lib.configupdater.configupdater.ConfigUpdater;

public class Config {
    private Logger logger;

    // A HashMap with String, FileConfiguration to store files data
    private HashMap<String, FileConfiguration> files;

    public Config(GreenBankMain plugin)  {
        this.logger = plugin.logger;
        files = new HashMap<>();

        String[] defaultFiles = new String[]{"config.yml"};

        File folder = plugin.getDataFolder();

        File newFile = new File(folder, defaultFiles[0]);
        if(!newFile.exists()) {
            folder.mkdirs();
            for (String file : defaultFiles) {
                plugin.saveResource(file, false);
            }

            logger.info("Created configuration files");
        }


    }
}
