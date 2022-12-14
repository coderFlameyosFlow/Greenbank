package org.flameyosflow.greenbank.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

import org.flameyosflow.greenbank.GreenBankMain;

public class JoinListener implements Listener {
    private final GreenBankMain greenBank;

    public JoinListener(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!greenBank.getEconomy().hasAccount(player)) {
            greenBank.getEconomy().createPlayerAccount(player);
        }
    }
}
