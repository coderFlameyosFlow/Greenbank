package org.flameyosflow.greenbank.economy;

import org.flameyosflow.greenbank.GreenBankMain;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class VaultLayer {
    private final GreenBankMain greenBank;

    public VaultLayer(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    public void hook() {
        // Actually hook into the vault api to the plugin.
        Bukkit.getServicesManager().register(Economy.class, greenBank.getEconomy(), this.greenBank, ServicePriority.Highest);
        greenBank.getLogger().info("Successfully hooked into the Vault API.");
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, greenBank.getEconomy());
        greenBank.getLogger().info("Successfully unhooked from the Vault API.");
    }
}