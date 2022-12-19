package org.flameyosflow.greenbank.economy;

import org.bson.Document;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.flameyosflow.greenbank.GreenBankMain;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static org.flameyosflow.greenbank.utils.MessageUtils.colorMessage;

public class VaultEconomySupport implements Economy {
    private final GreenBankMain greenBank;

    public VaultEconomySupport(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    @Override
    public String format(long amount) {
        return null;
    }

    @Override
    public boolean hasAccount(String playerUUID) {
        return greenBank.getMongoConnect().getPlayerDataCollection().find(eq("uuid", playerUUID)).first() != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return greenBank.getMongoConnect().getPlayerDataCollection().find(new Document().append("uuid", player.getUniqueId().toString())).first() != null;
    }

    @Override
    public long getBalance(String playerId) {
        if (hasAccount(playerId)) {
            return greenBank.getMongoConnect().getBalance(UUID.fromString(playerId));
        }
        return 0L;
    }

    @Override
    public long getBalance(OfflinePlayer player) {
        if (hasAccount(player)) {
            return greenBank.getMongoConnect().getBalance(player.getUniqueId());
        }
        return 0L;
    }

    @Override
    public boolean has(String playerId, long amount) {
        if (!hasAccount(playerId)) {
            return false;
        }
        return getBalance(playerId) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, long amount) {
        if (!hasAccount(player)) {
            return false;
        }
        return getBalance(player) >= amount;
    }

    @Override
    public void withdrawPlayer(OfflinePlayer player, long amount) {
        Player playerTwo = (Player) player;
        long balance = greenBank.getMongoConnect().getBalance(playerTwo.getUniqueId());
        if (!hasAccount(player)) {
            new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("player-does-not-exist")));
            return;
        }

        try {
            greenBank.getMongoConnect().setBalance(balance - amount, playerTwo.getUniqueId());

            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("money-paid-success")));
            new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, colorMessage(greenBank.getMessagesConfigFile().getString("money-paid-success")));
        } catch (Exception error) {
            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
            new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
        }
    }

    @Override
    public void depositPlayer(OfflinePlayer player, long amount) {
        Player playerTwo = (Player) player;
        long balance = greenBank.getMongoConnect().getBalance(playerTwo.getUniqueId());
        if (!hasAccount(player)) {
            new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("player-does-not-exist")));
            return;
        }

        try {
            greenBank.getMongoConnect().setBalance(balance + amount, playerTwo.getUniqueId());

            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("received-money-success")));
            new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, colorMessage(greenBank.getMessagesConfigFile().getString("received-money-success")));

        } catch (Exception error) {
            playerTwo.sendMessage(colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
            new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, colorMessage(greenBank.getMessagesConfigFile().getString("not-enough-money")));
        }
    }

    @Override
    public void createPlayerAccount(OfflinePlayer player) {
        if (!hasAccount(player)) {
            greenBank.getMongoConnect().addNewPlayer((Player) player);
        }
    }
}
