package org.flameyosflow.greenbank.economy;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.utils.Numbers;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultEconomySupport implements Economy {
    private GreenBankMain greenBank;
    @Override
    public boolean isEnabled() {
        return greenBank.isEnabled();
    }

    @Override
    public String getName() {
        return "GreenBank";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return Numbers.displayCurrency(BigDecimal.valueOf(amount), greenBank);
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Deprecated
    @Override
    public boolean hasAccount(String playerName) {
        
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        
        return false;
    }

    @Deprecated
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        
        return false;
    }

    @Deprecated
    @Override
    public double getBalance(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        UUID playerUUID = player.getUniqueId();
        return greenBank.playerBank.get(playerUUID);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        UUID playerUUID = player.getUniqueId();
        return greenBank.playerBank.get(playerUUID);
    }

    @Deprecated
    @Override
    public double getBalance(String playerName, String world) {
        Player player = Bukkit.getPlayer(playerName);
        UUID playerUUID = player.getUniqueId();
        return greenBank.playerBank.get(playerUUID);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        
        return 0;
    }

    @Deprecated
    @Override
    public boolean has(String playerName, double amount) {
        
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        
        return false;
    }

    @Deprecated
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        
        return false;
    }

    @Deprecated
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        
        return null;
    }

    @Deprecated
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        
        return null; 
    }

    @Deprecated
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        
        return null;
    }

    @Deprecated
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        
        return null;
    }

    @Deprecated
    @Override
    public EconomyResponse createBank(String name, String player) {
        
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        
        return null;
    }

    @Deprecated
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        
        return null;
    }

    @Deprecated
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        
        return null;
    }

    @Override
    public List<String> getBanks() {
        
        return null;
    }

    @Deprecated
    @Override
    public boolean createPlayerAccount(String playerName) {
        
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        
        return false;
    }

    @Deprecated
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        
        return false;
    }
    
}
