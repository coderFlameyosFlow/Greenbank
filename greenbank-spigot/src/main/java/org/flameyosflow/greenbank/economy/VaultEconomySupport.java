package org.flameyosflow.greenbank.economy;

import java.util.List;
import java.util.UUID;

import org.bson.Document;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.flameyosflow.greenbank.GreenBankMain;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import static com.mongodb.client.model.Filters.eq;
import static org.flameyosflow.greenbank.utils.MessageUtils.colorMessage;

public class VaultEconomySupport implements Economy {
    private final GreenBankMain greenBank;

    public VaultEconomySupport(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }
    
    @Override
    public boolean isEnabled() {
        return greenBank.isEnabled();
    }

    @Override
    public String getName() {
        return "GreenBank Economy";
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
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerUUID) {
        return greenBank.getMongoConnect().getPlayerDataCollection().find(
                eq("uuid", playerUUID)).first() != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        String playerUUID = player.getUniqueId().toString();
        return greenBank.getMongoConnect().getPlayerDataCollection().find(new Document().append("uuid", playerUUID)).first() != null;
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
        if (hasAccount(playerId)) {
            return greenBank.getMongoConnect().getBalance(UUID.fromString(playerId));
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        if (hasAccount(player)) {
            return greenBank.getMongoConnect().getBalance(player.getUniqueId());
        }
        return 0;
    }

    @Deprecated
    @Override
    public double getBalance(String playerName, String world) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return 0;
    }

    @Override
    public boolean has(String playerId, double amount) {
        if (!hasAccount(playerId)) {
            return false;
        }
        return getBalance(playerId) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        if (!hasAccount(player)) {
            return false;
        }
        return getBalance(player) >= amount;
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

    @Override
    public EconomyResponse withdrawPlayer(String playerId, double amount) {
        long balance = greenBank.getMongoConnect().getBalance(UUID.fromString(playerId));
        if (!hasAccount(playerId)) {
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("player-does-not-exist")));
        }

        Player player = Bukkit.getPlayer(UUID.fromString(playerId));

        try {
            greenBank.getMongoConnect().setBalance((long) (balance - amount), UUID.fromString(playerId));

            player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("money-paid-success")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, colorMessage(greenBank.getMessagesConfigFile().getString("money-paid-success")));
        } catch (Exception error) {
            player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Player playerTwo = (Player) player;
        long balance = greenBank.getMongoConnect().getBalance(playerTwo.getUniqueId());
        if (!hasAccount(player)) {
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("player-does-not-exist")));
        }

        try {
            greenBank.getMongoConnect().setBalance((long) (balance - amount), playerTwo.getUniqueId());

            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("money-paid-success")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, colorMessage(greenBank.getMessagesConfigFile().getString("money-paid-success")));
        } catch (Exception error) {
            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
        }
    }

    @Deprecated
    @Override
    public EconomyResponse withdrawPlayer(String playerId, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return null; 
    }

    @Override
    public EconomyResponse depositPlayer(String playerId, double amount) {
        long balance = greenBank.getMongoConnect().getBalance(UUID.fromString(playerId));
        if (!hasAccount(playerId)) {
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("player-does-not-exist")));
        }

        Player player = Bukkit.getPlayer(UUID.fromString(playerId));


        try {
            greenBank.getMongoConnect().setBalance((long) (balance + amount), UUID.fromString(playerId));

            player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("received-money-success")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, colorMessage(greenBank.getMessagesConfigFile().getString("received-money-success")));
        } catch (Exception error) {
            player.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Player playerTwo = (Player) player;
        long balance = greenBank.getMongoConnect().getBalance(playerTwo.getUniqueId());
        if (!hasAccount(player)) {
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("player-does-not-exist")));
        }

        try {

            greenBank.getMongoConnect().setBalance((long) (balance + amount), playerTwo.getUniqueId());

            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("received-money-success")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, colorMessage(greenBank.getMessagesConfigFile().getString("received-money-success")));

        } catch (Exception error) {
            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
            return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
        }
    }

    @Deprecated
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return null;
    }

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
    public EconomyResponse bankWithdraw(String Id, double amount) {
        
        return withdrawPlayer(Id, amount);
    }

    @Override
    public EconomyResponse bankDeposit(String Id, double amount) {

        return depositPlayer(Id, amount);
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

    @Override
    public boolean createPlayerAccount(String playerId) {
        Player player = Bukkit.getPlayer(UUID.fromString(playerId));
        UUID playerID = player.getUniqueId();

        if (playerID == null) return false;

        if (!hasAccount(player)) {
            greenBank.getMongoConnect().addNewPlayer(player);
        }

        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (!hasAccount(player)) {
            greenBank.getMongoConnect().addNewPlayer((Player) player);
        }
        return true;
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
