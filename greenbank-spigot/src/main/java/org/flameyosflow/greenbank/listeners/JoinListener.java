package org.flameyosflow.greenbank.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.api.economy.VaultEconomySupport;

public class JoinListener implements Listener {
    private final VaultEconomySupport economy;

    public JoinListener(GreenBankMain greenBank) {
        this.economy = greenBank.getEconomy();
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!economy.hasAccount(player)) economy.createPlayerAccount(player);
    }
}
