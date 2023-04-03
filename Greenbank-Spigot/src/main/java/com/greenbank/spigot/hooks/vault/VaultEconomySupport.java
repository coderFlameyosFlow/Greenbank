package com.greenbank.spigot.hooks.vault;

import com.greenbank.api.IEconomy;
import com.greenbank.api.IGreenbank;
import com.greenbank.api.database.DatabaseHandler;
import com.greenbank.spigot.utils.MessageUtils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.OfflinePlayer;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

public class VaultEconomySupport implements Economy, IEconomy {
    private final IGreenbank greenBank;
    private final DatabaseHandler databaseConnect;
    private final String playerDoesNotExist;

    public VaultEconomySupport(IGreenbank greenBank) {
        this.greenBank = greenBank;
        this.databaseConnect = greenBank.getDatabaseConnect();
        this.playerDoesNotExist = greenBank.getMessagesSettings().getPlayerDoesNotExist();
    }

    @Override
    public boolean isEnabled() {
        return greenBank.isEnabled();
    }

    @Override
    public String getName() {
        return "Greenbank";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    @NotNull
    public String format(double amount) {
        if (amount < 1000) return Double.toString(amount);
        char[] letters = {'K', 'M', 'B', 'T', 'P', 'E'};
        double value = amount;
        int index = 0;
        do {
            value /= 1000;
            index++;
        } while (value / 1000 >= 1);
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return String.format("%s%s", decimalFormat.format(value), letters[index]);
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerId) {
        return databaseConnect.playerNotNull(UUID.fromString(playerId));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return databaseConnect.playerNotNull(player.getUniqueId());
    }

    @Override
    public boolean hasAccount(String playerId, String worldName) {
        return hasAccount(playerId);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerId) {
        return hasAccount(playerId) ? databaseConnect.getMoney(UUID.fromString(playerId)) : 0.0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return hasAccount(player) ? databaseConnect.getMoney(player.getUniqueId()) : 0.0;
    }

    @Override
    public double getBalance(String playerId, String world) {
        return getBalance(playerId);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerId, double amount) {
        return getBalance(playerId) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerId, String worldName, double amount) {
        return has(playerId, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerId, double amount) {
        double balance = getBalance(playerId);
        try {
            return hasAccount(playerId) && databaseConnect.setMoney(UUID.fromString(playerId), balance - amount) ?
                   new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null) :
                   new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(this.playerDoesNotExist));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        double balance = getBalance(player);
        try {
            return hasAccount(player) && databaseConnect.setMoney(player.getUniqueId(), balance - amount) ?
                   new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null) :
                   new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(this.playerDoesNotExist));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String playerId, double amount) {
        double balance = getBalance(playerId);
        try {
            return hasAccount(playerId) && databaseConnect.setMoney(UUID.fromString(playerId), balance + amount) ?
                   new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null) :
                   new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(this.playerDoesNotExist));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        double balance = getBalance(player);
        try {
            return !hasAccount(player) && databaseConnect.setMoney(player.getUniqueId(), balance + amount) ?
                    new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null) :
                    new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(this.playerDoesNotExist));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerId, String worldName, double amount) {
        return depositPlayer(playerId, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

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

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

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

    @Override
    public boolean createPlayerAccount(String playerId) {
        databaseConnect.addNewPlayerIfAbsent(UUID.fromString(playerId));
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        databaseConnect.addNewPlayerIfAbsent(player.getUniqueId());
        return true;
    }

    @Override
    public boolean createPlayerAccount(@NotNull String playerId, String worldName) {
        return createPlayerAccount(playerId);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
