package com.greenbank.api;

import com.greenbank.api.database.DatabaseHandler;
import com.greenbank.api.settings.DatabaseSettings;
import com.greenbank.api.settings.MessagesSettings;
import com.greenbank.api.settings.Settings;

import dev.dejvokep.boostedyaml.YamlDocument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.logging.Logger;

public interface IGreenbank {
    /**
     * Returns the folder that the plugin data's files are located in. The
     * folder may not yet exist.
     *
     * @return The folder
     */
    @NotNull File getDataFolder();

    /**
     * Returns the folder that the plugin data's files are located in, but as a Path. The
     * folder may not yet exist.
     * <p></p>
     * USE THIS UNLESS NECESSARY
     * @return The folder
     */
    @NotNull Path getDataDirectory();

    /**
     * Gets an embedded resource in this plugin
     *
     * @param filename Filename of the resource
     * @return File if found, otherwise null
     */
    @Nullable InputStream getResource(@NotNull String filename);

    /**
     * Returns a value indicating whether this plugin is currently
     * enabled
     *
     * @return true if this plugin is enabled, otherwise false
     */
    boolean isEnabled();

    void disablePlugin();

    /**
     * Returns the plugin logger associated with this server's logger. The
     * returned logger automatically tags all log messages with the plugin's
     * name.
     *
     * @return Logger associated with this plugin
     */
    @NotNull Logger getLogger();

    @NotNull String getName();

    YamlDocument getConfigFile();

    Settings getConfigSettings();

    DatabaseSettings getDatabaseSettings();

    MessagesSettings getMessagesSettings();

    IEconomy getEconomy();

    DatabaseHandler getDatabaseConnect();
}
