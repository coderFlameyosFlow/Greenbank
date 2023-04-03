package com.greenbank.spigot.commands;

import com.greenbank.spigot.commands.manager.GreenbankCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.greenbank.spigot.utils.MessageUtils.colorMessage;

public class CreateAccountCommand extends GreenbankCommand {
    @Command({ "create-account", "create-player-account"})
    @CommandPermission("greenbank.user.balance")
    @Description("Check your balance or other player's balance.")
    public void onCommand(@NotNull Player player) {
        if (plugin.getConfigSettings().shouldCreateAccountOnJoin()) {
            player.sendMessage(colorMessage("&cYou already have an account!"));
            return;
        }

        economy.createPlayerAccount(player);
        player.sendMessage(colorMessage("&aSuccessfully created player account!"));
    }
}
