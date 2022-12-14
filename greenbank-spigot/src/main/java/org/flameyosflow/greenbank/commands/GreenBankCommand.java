package org.flameyosflow.greenbank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import static org.flameyosflow.greenbank.utils.MessageUtils.colorMessage;

/**
 * DO NOT MIX THIS UP WITH Green***b***ankCommand.
 *
 * <p>
 * The main class for ALL greenbank commands, use this instead of CommandExecutor.
 * This is useful for for many stuff, such as overriding commands, and comes with pre-made messages (from CommandSupport)
 * </p>
 *
 * @author FlameyosFlow
 */
public abstract class GreenBankCommand extends CommandSupport implements CommandExecutor {
    public static void register(String name, @NotNull GreenBankCommand wrapper) {
        PluginCommand command = Bukkit.getPluginCommand(name);

        if (command == null) return;

        command.setExecutor(wrapper);

        // add command to internal register
        add(name, command);
    }

    public abstract boolean execute(@NotNull CommandSender sender, Command command, String label, String[] args);

    protected void sendGreenbankHelpMessage(@NotNull CommandSender sender) {
        String GREENBANK_TOP_BOTTOM = "&7&l====================| &2&lGreen&a&lBank &e&lHelp &7&l|====================";
        sender.sendMessage(colorMessage(GREENBANK_TOP_BOTTOM));
        sender.sendMessage(colorMessage(" "));
        sender.sendMessage(colorMessage("&7/greenbank &areload: "));
        sender.sendMessage(colorMessage("&7Reloads ALL configs (messages, database and normal)."));
        sender.sendMessage(colorMessage(" "));
        sender.sendMessage(colorMessage(GREENBANK_TOP_BOTTOM));
    }

    protected void sendEconomyHelpMessage(@NotNull CommandSender sender) {
        String ECONOMY_TOP_BOTTOM = "&7&l====================| &2&lEconomy &e&lHelp &7&l|====================";
        sender.sendMessage(colorMessage(ECONOMY_TOP_BOTTOM));
        sender.sendMessage(colorMessage(" "));
        sender.sendMessage(colorMessage("&7/eco &agive <player> <amount>: "));
        sender.sendMessage(colorMessage("&7Gives a player an amount of money."));
        sender.sendMessage(colorMessage(" "));
        sender.sendMessage(colorMessage("&7/eco &aremove <player> <amount>: "));
        sender.sendMessage(colorMessage("&7Removes an amount of money from the player."));
        sender.sendMessage(colorMessage(" "));
        sender.sendMessage(colorMessage("&7/eco &aset <player> <amount>: "));
        sender.sendMessage(colorMessage("&7Sets a player's balance to an amount of money."));
        sender.sendMessage(colorMessage(" "));
        sender.sendMessage(colorMessage("&7/eco &areset <player> <amount>: "));
        sender.sendMessage(colorMessage("&7Resets a player's balance to the default starting balance."));
        sender.sendMessage(colorMessage(" "));
        sender.sendMessage(colorMessage(ECONOMY_TOP_BOTTOM));
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, command, label, args);
    }
}
