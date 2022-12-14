package org.flameyosflow.greenbank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.api.errors.CannotFindUserException;
import org.jetbrains.annotations.NotNull;

import static org.flameyosflow.greenbank.utils.MessageUtils.colorMessage;

/**
 *  Administrative commands to handle some green money!
 *
 * @author FlameyosFlow
 * @since 1.0.0 BUILD 1
 */
public class EcoCommand extends GreenBankCommand {
    private final GreenBankMain greenBank;

    public EcoCommand(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    private void givePlayer(CommandSender sender, Player player, long amount) throws CannotFindUserException {
        if (playerHasNotPlayedBefore(player)) {
            throw new CannotFindUserException(PLAYER_DOES_NOT_EXIST);
        }

        greenBank.getEconomy().depositPlayer(player, amount);

        sender.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("money-given-success")
                .replace("%amount%", String.valueOf(amount))
                .replace("%player%", player.getName())));
        player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("given-money-success")
                .replace("%amount%", String.valueOf(amount))
                .replace("%total%", String.valueOf(greenBank.getEconomy().getBalance(player) + amount))));

    }

    private void removePlayer(CommandSender sender, Player player, long amount) throws CannotFindUserException {
        if (playerHasNotPlayedBefore(player)) {
            throw new CannotFindUserException(PLAYER_DOES_NOT_EXIST);
        }

        greenBank.getEconomy().withdrawPlayer(player, amount);
        sender.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("removed-money-success")
                .replace("%amount%", String.valueOf(amount))
                .replace("%player%", player.getName())));
        player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("player-removed-money-success")
                .replace("%amount%", String.valueOf(amount))
                .replace("%total%", String.valueOf(greenBank.getEconomy().getBalance(player) - amount))));
    }

    private void setPlayer(CommandSender sender, Player player, long amount) throws CannotFindUserException {
        if (playerHasNotPlayedBefore(player)) {
            throw new CannotFindUserException(PLAYER_DOES_NOT_EXIST);
        }

        greenBank.getMongoConnect().setBalance(amount, player.getUniqueId());

        sender.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("set-money-success")
                .replace("%amount%", String.valueOf(amount))
                .replace("%player%", String.valueOf(player.getName()))));
        player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("player-money-set-success")
                .replace("%amount%", String.valueOf(amount))));
    }

    private void resetPlayer(CommandSender sender, Player player) throws CannotFindUserException {
        if (playerHasNotPlayedBefore(player)) {
            throw new CannotFindUserException(PLAYER_DOES_NOT_EXIST);
        }

        greenBank.getMongoConnect().setBalance(greenBank.getConfigFile().getLong("default-starting-balance"), player.getUniqueId());

        sender.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("reset-money-success")));
        player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("player-money-reset-success")));
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, Command command, String label, String[] args) {

        // If the player has permission then execute.
        if (senderDoesNotHavePermission(sender, "greenbank.admin.eco")) {
            sender.sendMessage(colorMessage(NOT_ENOUGH_PERMISSIONS));
            return false;
        }

        if (args.length != 3) {
            sender.sendMessage(colorMessage("&cUsage: /eco <command> <player> <amount>"));
            return false;
        }

        Player playerTwo = Bukkit.getPlayer(args[1]);

        long amount = Long.parseLong(args[2]);

        if (args[0].equalsIgnoreCase("give") &&
            args[1].equalsIgnoreCase(playerTwo.getName()) &&
            args[2].equalsIgnoreCase(String.valueOf(amount)))
        {
            try {
                givePlayer(sender, playerTwo, amount);
            } catch (CannotFindUserException error) {
                sender.sendMessage(colorMessage(PLAYER_DOES_NOT_EXIST));
            }

        } else if (args[0].equalsIgnoreCase("remove") &&
                   args[1].equalsIgnoreCase(playerTwo.getName()) &&
                   args[2].equalsIgnoreCase(String.valueOf(amount))) {
            try {
                removePlayer(sender, playerTwo, amount);
            } catch (CannotFindUserException error) {
                sender.sendMessage(colorMessage(PLAYER_DOES_NOT_EXIST));
            }

        } else if (args[0].equalsIgnoreCase("set") &&
                   args[1].equalsIgnoreCase(playerTwo.getName()) &&
                   args[2].equalsIgnoreCase(String.valueOf(amount))) {
            try {
                setPlayer(sender, playerTwo, amount);
            } catch (CannotFindUserException error) {
                sender.sendMessage(colorMessage(PLAYER_DOES_NOT_EXIST));
            }


        } else if (args[0].equalsIgnoreCase("reset") &&
                   args[1].equalsIgnoreCase(playerTwo.getName()) &&
                   args[2].equalsIgnoreCase(String.valueOf(amount))) {

            try {
                resetPlayer(sender, playerTwo);
            } catch (CannotFindUserException error) {
                sender.sendMessage(colorMessage(PLAYER_DOES_NOT_EXIST));
            }
        } else {
            sendEconomyHelpMessage(sender);
        }

        return true;
    }
}
