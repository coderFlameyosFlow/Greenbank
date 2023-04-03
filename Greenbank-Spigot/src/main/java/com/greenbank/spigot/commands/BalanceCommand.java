package com.greenbank.spigot.commands;

import com.greenbank.spigot.commands.manager.GreenbankCommand;

import org.apache.commons.lang.StringUtils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.greenbank.spigot.utils.MessageUtils.colorMessage;

public class BalanceCommand extends GreenbankCommand {
    @Command({ "bal", "money" })
    @CommandPermission("greenbank.user.balance")
    @Description("Check your balance or other player's balance.")
    public void onCommand(@NotNull Player player, @Optional OfflinePlayer target) {
        if (target == null) {
            double balance = economy.getBalance(player);
            String balanceString = messages.getPlayerBalance();
            StringUtils.replace(balanceString, "%balance%", economy.format(balance));
            player.sendMessage(colorMessage(balanceString));
        } else {
            double balance = economy.getBalance(target);
            String otherBalance = messages.getOtherPlayerBalance();
            StringUtils.replace(otherBalance, "%balance%", economy.format(balance));
            StringUtils.replace(otherBalance, "%player%", target.getName());
            player.sendMessage(colorMessage(otherBalance));
        }
    }
}
