package org.flameyosflow.greenbank.commands.noteconomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.commands.GreenBankCommand;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static org.flameyosflow.greenbank.utils.MessageUtils.colorMessage;

/**
 * DO NOT MIX THIS UP WITH Green***B***ankCommand
 *
 * @author FlameyosFlow
 */
public class GreenbankCommand extends GreenBankCommand {
    private final GreenBankMain greenBank;

    public GreenbankCommand(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    public void configError(@NotNull CommandSender sender) {
        sender.sendMessage(colorMessage("&cUh oh! something went wrong, please check console!"));
        sender.sendMessage(" ");
        sender.sendMessage(colorMessage("&cMake sure your database configuration is correct!"));
        sender.sendMessage(colorMessage("&cAnd make sure you didn't put any unsupported placeholders in messages.yml's messages!"));
        sender.sendMessage(colorMessage("&4If you think this is not your fault, make sure to report this to the developer."));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, Command command, String label, String[] args) {
        if (senderDoesNotHavePermission(sender, "greenbank.admin.gb")) {
            sender.sendMessage(colorMessage(NOT_ENOUGH_PERMISSIONS));
            return false;
        }

        if (args.length == 0) {
            sendGreenbankHelpMessage(sender);
        }

        if (args[0].equalsIgnoreCase("reload")) {
            try {
                greenBank.initConfig();
                sender.sendMessage(colorMessage("&aSuccessfully reloaded all GreenBank's configuration files"));
            } catch (IOException error) {
                configError(sender);
            }
        }

        if (args[0].equalsIgnoreCase("version")) {
            sender.sendMessage(colorMessage("&7Greenbank's version is: " + "&c&l" + greenBank.getVersion()));
        }
        return true;
    }
}
