package org.flameyosflow.greenbank.api.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.dejvokep.boostedyaml.spigot.SpigotSerializer;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;

import java.io.File;
import java.io.IOException;

import org.flameyosflow.greenbank.GreenBankMain;

public class MessagesConfig {
    private final GreenBankMain plugin;

    public MessagesConfig(GreenBankMain plugin) {
        this.plugin = plugin;
    }

    public YamlDocument messagesConfig() throws IOException {
        return YamlDocument.create(
                new File(plugin.getDataFolder(), "messages.yml"),
                plugin.getConfigYaml("messages.yml"),
                GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build(),
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
    }
}