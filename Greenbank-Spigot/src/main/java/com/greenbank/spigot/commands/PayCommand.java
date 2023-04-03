package com.greenbank.spigot.commands;

import com.greenbank.spigot.commands.manager.GreenbankCommand;

import net.milkbowl.vault.economy.EconomyResponse;

import org.apache.commons.lang.StringUtils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.greenbank.spigot.utils.MessageUtils.colorMessage;

public class PayCommand extends GreenbankCommand {
    @Command({ "pay", "transfer", "wire" })
    @CommandPermission("greenbank.user.pay")
    @Description("Pay/wire your money to someone else!")
    public void onCommand(@NotNull Player player, OfflinePlayer target, double amount) {
        if (!player.hasPermission("greenbank.user.pay")) {
            player.sendMessage(colorMessage(messages.getNotEnoughPermissions()));
            return;
        }

       if (target == null || !economy.hasAccount(player)) {
           player.sendMessage(colorMessage(messages.getPlayerDoesNotExist()));
       } else {
           EconomyResponse response1 = economy.depositPlayer(target, amount);
           if (response1.transactionSuccess()) {
               String message = messages.getReceivedMoneySuccess();
               StringUtils.replace(message, "%amount%", economy.format(amount));
               StringUtils.replace(message, "%player%", target.getName());
               StringUtils.replace(message, "%total%", economy.format(economy.getBalance(target)));
               Player player1 = target.getPlayer();
               if (player1 != null) {
                   player1.sendMessage(colorMessage(message));
               }
           } else {
               player.sendMessage(colorMessage("&cThere was an &4&lun-logged error &cpaying money to someone, if you think this is an error, please contact server administrators and they can figure this out."));
           }

           EconomyResponse response = economy.withdrawPlayer(player, amount);
           if (response.transactionSuccess()) {
               String message = messages.getPaidMoneySuccess();
               StringUtils.replace(message, "%amount%", economy.format(amount));
               StringUtils.replace(message, "%player%", player.getName());
               StringUtils.replace(message, "%total%", economy.format(economy.getBalance(player)));
               player.sendMessage(colorMessage(message));
           }
       }
    }
}
