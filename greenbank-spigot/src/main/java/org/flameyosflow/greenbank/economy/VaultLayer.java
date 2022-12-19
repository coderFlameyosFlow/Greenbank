package org.flameyosflow.greenbank.economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import org.flameyosflow.greenbank.GreenBankMain;

public class VaultLayer {
    private final GreenBankMain greenBank;

    public VaultLayer(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    public void hook() {
        // Actually hook into the custom vault api to the plugin.
        Bukkit.getServicesManager().register(Economy.class, greenBank.getEconomy(), this.greenBank, ServicePriority.Highest);
        greenBank.getLogger().info("Successfully hooked into the Vault API.");
    }
}
