package org.flameyosflow.greenbank.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.api.errors.CannotFindUserException;
import org.flameyosflow.greenbank.api.errors.CannotPerformTransactionOnSelf;
import org.flameyosflow.greenbank.api.errors.NotEnoughMoney;

import org.jetbrains.annotations.NotNull;

import static org.flameyosflow.greenbank.utils.MessageUtils.colorMessage;

/**
 * Pay someone some green money!
 *
 * @author FlameyosFlow
 * @since 1.0.0 BUILD 1
 */
public class PayCommand extends GreenBankCommand {
    private final GreenBankMain greenBank;

    public PayCommand(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    private void payPlayer(@NotNull Player player, @NotNull Player playerTwo, long amount) throws NotEnoughMoney, CannotFindUserException, CannotPerformTransactionOnSelf {
        if (playerHasNotPlayedBefore(playerTwo)) {
            throw new CannotFindUserException(PLAYER_DOES_NOT_EXIST);
        }

        if (!greenBank.getEconomy().has(player, amount)) {
            throw new NotEnoughMoney(NOT_ENOUGH_MONEY);
        }

        if (player.getUniqueId() == playerTwo.getUniqueId()) {
            throw new CannotPerformTransactionOnSelf("Player disallowed to pay themself!");
        }

        greenBank.getEconomy().withdrawPlayer(player, amount);
        player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("money-paid-success")
                .replace("%amount%", Long.toString(amount))
                .replace("%player%", playerTwo.getName())
                .replace("%total%", Long.toString((long) (greenBank.getEconomy().getBalance(player) - amount)))));

        greenBank.getEconomy().depositPlayer(playerTwo, amount);
        player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("received-money-success")
                .replace("%amount%", String.valueOf(amount))
                .replace("%player%", player.getName())
                .replace("%total%", Long.toString((long) (greenBank.getEconomy().getBalance(playerTwo) - amount)))));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (notPlayer(sender)) {
            sender.sendMessage(ONLY_PLAYER);
            return false;
        }

        if (playerDoesNotHavePermission(player, "greenbank.user.pay")) {
            sender.sendMessage(NOT_ENOUGH_PERMISSIONS);
            return false;
        }

        if (args.length != 2) {
            player.sendMessage(colorMessage("&cUsage: /pay <player> <amount>"));
            return false;
        }

        long amount = Long.parseLong(args[0]);
        Player playerTwo = Bukkit.getPlayer(args[1]);

        try {
            payPlayer(player, playerTwo, amount);
        } catch (CannotFindUserException error) {
            player.sendMessage(colorMessage(PLAYER_DOES_NOT_EXIST));
            return false;
        } catch (NotEnoughMoney error) {
            player.sendMessage(colorMessage(NOT_ENOUGH_MONEY));
            return false;
        } catch (CannotPerformTransactionOnSelf error) {
            player.sendMessage(colorMessage("&cCannot perform transaction on yourself, please choose someone."));
            return false;
        }
        return true;
    }
}
