package org.flameyosflow.greenbank.database.mongodb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.conversions.Bson

import org.bukkit.entity.Player

import org.flameyosflow.greenbank.GreenBankMain
import org.flameyosflow.greenbank.utils.annotations.Asynchronous

import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

/**
 * The MongoDatabase connection to the MongoDB database.
 *
 * @author FlameyosFlow
 */
@Suppress("UNCHECKED_CAST", "LiftReturnOrAssignment")
class MongoDBDatabaseConnect(private val greenBank: GreenBankMain) : MongoDBConnect<MongoClient?>() {
    /**
     * Connect to the MongoDB database.
     *
     * @return the mongo client.
     * @author FlameyosFlow
     * @since 1.0.0 BUILD 1
     */
    override fun createConnection(): MongoClient = MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(greenBank.databaseConfigFile!!.getString("mongodb-connection-uri")))
            .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
            .build())

    override fun getConnection(): Any? {
        val mongoDatabase = createConnection().getDatabase(greenBank.databaseConfigFile!!.getString("mongodb-database-name"))
        return mongoDatabase.getCollection(greenBank.databaseConfigFile!!.getString("mongodb-database-collection"))
    }
    val collection = (connection as MongoCollection<Document>)

    override fun closeConnection() = createConnection().close()

    override fun delete(delete: Any) {
        collection.deleteOne(delete as Bson)
    }
    @Asynchronous override fun setDocumentPlayerData(value: Any, identifier: String, uuid: String): Void? = try { CompletableFuture.runAsync({ collection.updateOne(Filters.eq("uuid", uuid), Updates.set(identifier, value), UpdateOptions().upsert(true)) }, Executors.newFixedThreadPool(30)).get() } catch (e: Exception) { throw RuntimeException(e) } finally { greenBank.logger.info("Completed asynchronous updating for user uuid: $uuid") }

    @Asynchronous override fun getDocumentPlayerData(identifier: String, uuid: String): Any? {
        val document = collection.find(Filters.eq("uuid", uuid)).first()
        assert(document != null)
        val foundDocument = collection.find(document!!).first()
        assert(foundDocument != null)
        return try { CompletableFuture.supplyAsync { foundDocument?.get(identifier)!! }.get() } catch (e: Exception) { throw RuntimeException(e) }
    }

    @Asynchronous fun addNewPlayer(player: Player): Boolean {
         try {
            collection.insertOne(Document()
                .append("uuid", player.uniqueId.toString()) // Add unique identifier
                .append("balance", greenBank.configFile!!.getLong("default-starting-balance")) // Add balance
                .append("bank-account", greenBank.configFile!!.getLong("default-starting-balance")) // Add bank account
                .append("has-infinite-money", false))
            return true
        } catch (error: Exception) {
            greenBank.logger.severe("Failed to insertOne new player (balance, uuid): " + error.message)
            error.printStackTrace()
            return false
        }
    }
    @Asynchronous fun getBalance(uuid: UUID): Long = getDocumentPlayerData("balance", uuid.toString()) as Long
    @Asynchronous fun setBalance(balance: Long, uuid: UUID): Void? = setDocumentPlayerData(balance, "balance", uuid.toString())
}