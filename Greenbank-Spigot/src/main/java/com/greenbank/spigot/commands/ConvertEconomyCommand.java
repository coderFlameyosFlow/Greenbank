package com.greenbank.spigot.commands;

import com.greenbank.spigot.commands.manager.GreenbankCommand;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

import org.jetbrains.annotations.NotNull;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.greenbank.spigot.utils.MessageUtils.colorMessage;

public class ConvertEconomyCommand extends GreenbankCommand {
    private boolean foundEconomy = false;

    @Command({ "bal", "money" })
    @CommandPermission("greenbank.user.balance")
    @Description("Check your balance or other player's balance.")
    public void onCommand(@NotNull CommandSender sender, String from) {
        List<RegisteredServiceProvider<Economy>> economies = new ArrayList<>(Bukkit.getServicesManager().getRegistrations(Economy.class));
        sender.sendMessage(colorMessage("&cConverting economy, this operation runs asynchronously and only has effect if the implementation is synchronous. \nWARNING: if the implementation is not thread-safe, it can lead to some errors."));
        economies.forEach(economy -> {
            Economy other = economy.getProvider();
            String otherName = other.getName();
            if (otherName.equalsIgnoreCase(from) && !otherName.equals("Greenbank")) {
                this.foundEconomy = true;
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    CompletableFuture.runAsync(() -> {
                        if (other.hasAccount(offlinePlayer)) this.switchMoney(other, offlinePlayer);
                    });
                }
            }
        });

        if (!foundEconomy) {
            sender.sendMessage(colorMessage("&cOperation failed because no economy matched: '" + from + "'"));
        } else {
            sender.sendMessage(colorMessage("&aSuccessfully converted! &e&lTEST BEFORE CONTINUING."));
        }
    }


    private void switchMoney(Economy other, OfflinePlayer offlinePlayer) {
        double balance = other.getBalance(offlinePlayer);
        other.withdrawPlayer(offlinePlayer, balance);
        this.economy.depositPlayer(offlinePlayer, balance);
    }
}
