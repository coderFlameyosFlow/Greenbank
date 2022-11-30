package org.flameyosflow.greenbank.economy;

import org.flameyosflow.greenbank.economy.layers.GreenbankLayer;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.math.BigDecimal;

public class VaultLayer implements GreenbankLayer {
    private Plugin plugin;
    private Economy adapter;

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return adapter.hasAccount(player);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return adapter.createPlayerAccount(player);
    }

    @Override
    public BigDecimal getPlayerBalance(OfflinePlayer player) {
        return BigDecimal.valueOf(adapter.getBalance(player));
    }

    @Override
    public boolean depositToPlayer(OfflinePlayer player, BigDecimal amount) {
        return adapter.depositPlayer(player, amount.doubleValue()).transactionSuccess();
    }

    @Override
    public boolean withdrawToPlayer(OfflinePlayer player, BigDecimal amount) {
        return adapter.withdrawPlayer(player, amount.doubleValue()).transactionSuccess();
    }

    @Override
    public String getName() {
        return "Vault Compatibility Layer";
    }

    @Override
    public String getBackendName() {
        return adapter.getName();
    }

    @Override
    public void enable(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        this.plugin = null;
        this.adapter = null;
    }

    @Override
    public String getPluginName() {
        return "Vault";
    }

    @Override
    public String getPluginVersion() {
        return plugin == null ? null : plugin.getDescription().getVersion();
    }

    @Override
    public boolean hasWorldAccount(OfflinePlayer player, String worldName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public BigDecimal getWorldPlayerBalance(OfflinePlayer player, String worldName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean depositToPlayerWorld(OfflinePlayer player, BigDecimal amount, String worldName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean withdrawToPlayerWorld(OfflinePlayer player, BigDecimal amount, String worldName) {
        // TODO Auto-generated method stub
        return false;
    }
}