package org.flameyosflow.greenbank.database.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import org.bson.Document;

import org.bukkit.entity.Player;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.utils.annotations.Asynchronous;

import java.util.UUID;

/**
 * The MongoDatabase connection to the MongoDB database.
 *
 * @author FlameyosFlow
 */


public class MongoDBDatabaseConnect {
    private MongoCollection<Document> playerData;
    private final GreenBankMain greenBank;

    public MongoDBDatabaseConnect(GreenBankMain greenBank) {
        this.greenBank = greenBank;
    }

    public MongoCollection<Document> getPlayerDataCollection() {
        return playerData;
    }

    public void setPlayerDataCollection(MongoCollection<Document> pD) {
        playerData = pD;
    }

    /**
     * Connect to the MongoDB database, MongoDatabase's implementation is thread-safe, as said by its annotation.
     *
     * @author FlameyosFlow
     * @since 1.0.0 BUILD 1
     */
    @Asynchronous
    public void connect() {
        String databaseUri = greenBank.getDatabaseConfigFile().getString("mongodb-connection-uri");
        String databaseName = greenBank.getDatabaseConfigFile().getString("mongodb-database-name");
        String databaseCollection = greenBank.getDatabaseConfigFile().getString("mongodb-database-collection");

        try (MongoClient mongoClient = MongoClients.create(databaseUri)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
            setPlayerDataCollection(mongoDatabase.getCollection(databaseCollection));
        }
    }

    public void setDocumentPlayerData(Object value, String identifier, String uuid) {
        UpdateOptions options = new UpdateOptions().upsert(true);

        playerData.updateOne(eq("uuid", uuid), set(identifier, value), options);
        greenBank.getLogger().info("Player data updated successfully.");
    }

    public Object getDocumentPlayerData(String identifier, String uuid) {
        try {
            Document document = playerData.find(eq("uuid", uuid)).first();
            assert document != null;

            Document foundDocument = playerData.find(document).first();

            assert foundDocument != null;
            return foundDocument.get(identifier);
        } catch (AssertionError error) {
            greenBank.getLogger().severe("Document/foundDocument does not exist or is null: " + error + "\nError message: " + error.getMessage());
            return null;
        }
    }

    public void addNewPlayer(Player player) {
        try {
            playerData.insertOne(new Document()
                    .append("uuid", player.getUniqueId().toString()) // Add unique identifier
                    .append("balance", greenBank.getConfigFile().getLong("default-starting-balance")) // Add balance
                    .append("bank-account", greenBank.getConfigFile().getLong("default-starting-balance")) // Add bank account
                    .append("has-infinite-money", false)); // Add the boolean value for infinite money

        } catch (final Exception error) {
            greenBank.logError("Failed to insertOne new player (balance, uuid): " + error.getMessage());
            error.printStackTrace();
        }
    }

    public long getBalance(UUID uuid) {
        return (long) getDocumentPlayerData("balance", uuid.toString());
    }

    public void setBalance(long balance, UUID uuid) {
        setDocumentPlayerData(balance, "balance", uuid.toString());
    }
}
