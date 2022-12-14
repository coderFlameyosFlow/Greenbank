package org.flameyosflow.greenbank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.api.errors.CannotFindUserException;

import static org.flameyosflow.greenbank.utils.MessageUtils.colorMessage;

/**
 * Check the amount of someone's green money!
 *
 * @author FlameyosFlow
 * @since 1.0.0 BUILD 1
 */
public class BalanceCommand extends GreenBankCommand {
    private final GreenBankMain greenBank;

    public BalanceCommand(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    private void getPlayerBalance(Player player) throws CannotFindUserException {
        if (playerHasNotPlayedBefore(player)) {
            throw new CannotFindUserException(PLAYER_DOES_NOT_EXIST);
        }

        long balance = (long) greenBank.getEconomy().getBalance(player);
        String balanceString = Long.toString(balance);

        String balanceMessage = greenBank.getMessagesConfigFile().getString("other-player-balance")
                .replace("%balance%", balanceString)
                .replace("%player%", player.getName());
        player.sendMessage(colorMessage(balanceMessage));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        // Only allow players to execute this command.
        if (notPlayer(sender)) {
            sender.sendMessage(colorMessage(ONLY_PLAYER));
            return false;
        }

        // If the player has permission then execute.
        if (playerDoesNotHavePermission(player, "greenbank.user.balance")) {
            player.sendMessage(colorMessage(NOT_ENOUGH_PERMISSIONS));
            return false;
        }

        if (args.length == 1) {
            Player player2 = Bukkit.getPlayer(args[0]);
            try {
                getPlayerBalance(player2);
            } catch (CannotFindUserException error) {
                player.sendMessage(colorMessage(PLAYER_DOES_NOT_EXIST));
                return false;
            }
            return true;
        }

        try {
            getPlayerBalance(player);
        } catch (CannotFindUserException error) {
            player.sendMessage(colorMessage(PLAYER_DOES_NOT_EXIST));
            return false;
        }
        return true;
    }
}
