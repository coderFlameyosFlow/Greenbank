package com.greenbank.spigot.commands;

import com.greenbank.spigot.commands.manager.GreenbankCommand;
import dev.dejvokep.boostedyaml.YamlDocument;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.greenbank.spigot.utils.MessageUtils.colorMessage;

@Command({ "greenbank", "gb" })
@CommandPermission("greenbank.admin.greenbank")
@Description("The main command of the plugin, Greenbank.")
public class CommandGreenbank extends GreenbankCommand {
    @Subcommand("reload")
    @CommandPermission("greenbank.admin.greenbank.reload")
    @Description("Reload all configuration files.")
    public void onReloadCommand(@NotNull CommandSender sender) {
        sender.sendMessage(colorMessage("&aReloading..."));
        try {
            YamlDocument config = plugin.getConfigFile();
            config.save();
            config.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sender.sendMessage(colorMessage("Successfully reloaded Greenbank"));
    }
}
