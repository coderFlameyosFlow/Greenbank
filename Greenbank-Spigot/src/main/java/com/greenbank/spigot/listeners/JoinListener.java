package com.greenbank.spigot.listeners;

import com.greenbank.api.IGreenbank;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private final IGreenbank plugin;

    public JoinListener(IGreenbank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.getConfigSettings().shouldCreateAccountOnJoin()) {
            plugin.getDatabaseConnect().addNewPlayerIfAbsent(event.getPlayer().getUniqueId());
        }
    }
}
