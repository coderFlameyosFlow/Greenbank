package com.greenbank.spigot.commands;

import com.greenbank.spigot.commands.manager.GreenbankCommand;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.OfflinePlayer;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.apache.commons.lang.StringUtils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static com.greenbank.spigot.utils.MessageUtils.colorMessage;

@Command({ "eco", "econ", "economy" })
@CommandPermission("greenbank.admin.eco")
@Description("Administrative commands for managing other player's money and/or banks.")
public class EcoCommand extends GreenbankCommand {
    @Subcommand("give")
    public void onGiveCommand(@NotNull CommandSender sender, OfflinePlayer target, double amount) {
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(colorMessage(messages.getPlayerDoesNotExist()));
            return;
        }
        EconomyResponse response = economy.depositPlayer(target, amount);
        if (response.transactionSuccess()) {
            String formattedAmount = economy.format(amount);
            String targetName = target.getName();

            String givenMoney = messages.getMoneyGivenSuccess();
            StringUtils.replace(givenMoney, "%amount%", formattedAmount);
            StringUtils.replace(givenMoney, "%player%", targetName);

            String moneyGiven = messages.getGivenMoneySuccess();
            StringUtils.replace(moneyGiven, "%balance%", formattedAmount);
            StringUtils.replace(moneyGiven, "%total%", economy.format(economy.getBalance(target)));

            sender.sendMessage(colorMessage(givenMoney));
            Player player1 = target.getPlayer();
            if (player1 != null) {
                player1.sendMessage(colorMessage(moneyGiven));
            }
        } else {
            sender.sendMessage(colorMessage("&cThere was an &4&lun-logged error &cpaying money to someone, if you think this is an error, please contact server administrators and they can figure this out."));
        }
    }

    @Subcommand("take")
    public void onTakeCommand(@NotNull CommandSender sender, OfflinePlayer target, double amount) {
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(colorMessage(messages.getPlayerDoesNotExist()));
            return;
        }
        EconomyResponse response = economy.withdrawPlayer(target, amount);
        if (response.transactionSuccess()) {
            String targetName = target.getName();

            String givenMoney = messages.getRemovedMoneySuccess();
            String formattedAmount = Double.toString(amount);
            StringUtils.replace(givenMoney, "%amount%", formattedAmount);
            StringUtils.replace(givenMoney, "%player%", targetName);

            String moneyGiven = messages.getPlayerRemovedMoneySuccess();
            StringUtils.replace(moneyGiven, "%amount%", formattedAmount);
            StringUtils.replace(moneyGiven, "%total%", economy.format(economy.getBalance(target)));

            sender.sendMessage(colorMessage(givenMoney));
            Player player1 = target.getPlayer();
            if (player1 != null) {
                player1.sendMessage(colorMessage(moneyGiven));
            }
        } else {
            sender.sendMessage(colorMessage("&cThere was an &4&lun-logged error &cpaying money to someone, if you think this is an error, please contact server administrators and they can figure this out."));
        }

    }

    @Subcommand("set")
    public void onSetCommand(@NotNull CommandSender sender, OfflinePlayer target, double amount) {
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(colorMessage(messages.getPlayerDoesNotExist()));
            return;
        }

        try {
            plugin.getDatabaseConnect().setMoney(target.getUniqueId(), amount);
            String formattedAmount = economy.format(amount);
            String targetName = target.getName();

            String givenMoney = messages.getSetMoneySuccess();
            StringUtils.replace(givenMoney, "%amount%", formattedAmount);
            StringUtils.replace(givenMoney, "%player%", targetName);

            String moneyGiven = messages.getPlayerSetMoneySuccess();
            StringUtils.replace(moneyGiven, "%amount%", formattedAmount);

            sender.sendMessage(colorMessage(givenMoney));
            Player player1 = target.getPlayer();
            if (player1 != null) {
                player1.sendMessage(colorMessage(moneyGiven));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Subcommand("reset")
    public void onResetCommand(@NotNull CommandSender sender, Player target) {
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(colorMessage(messages.getPlayerDoesNotExist()));
            return;
        }
        try {
            plugin.getDatabaseConnect().setMoney(target.getUniqueId(), configSettings.getDefaultStartingBalance());
            String targetName = target.getName();
            String givenMoney = messages.getResetMoneySuccess();
            StringUtils.replace(givenMoney, "%player%", targetName);

            String moneyGiven = messages.getPlayerResetMoneySuccess();

            sender.sendMessage(colorMessage(givenMoney));
            Player player1 = target.getPlayer();
            if (player1 != null) {
                player1.sendMessage(colorMessage(moneyGiven));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
