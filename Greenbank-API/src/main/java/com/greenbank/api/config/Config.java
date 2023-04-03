package com.greenbank.api.config;

import java.io.IOException;
import java.io.File;
import java.util.Objects;

import com.greenbank.api.IGreenbank;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.dejvokep.boostedyaml.spigot.SpigotSerializer;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;

public class Config {
    public static YamlDocument configFile(IGreenbank plugin) throws IOException {
        return YamlDocument.create(
                new File(plugin.getDataFolder(), "config.yml"),
                Objects.requireNonNull(plugin.getResource("config.yml")),
                GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build(),
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
    }
}
