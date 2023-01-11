package org.flameyosflow.greenbank

import dev.dejvokep.boostedyaml.YamlDocument

import net.milkbowl.vault.economy.Economy

import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.scheduler.BukkitRunnable
import org.flameyosflow.greenbank.api.config.Config
import org.flameyosflow.greenbank.api.config.DatabaseConfig
import org.flameyosflow.greenbank.api.config.MessagesConfig
import org.flameyosflow.greenbank.api.economy.VaultEconomySupport
import org.flameyosflow.greenbank.database.mongodb.MongoDBDatabaseConnect

import java.io.IOException; import java.io.InputStream; import java.util.*

class GreenBankMain : GreenBankSupport() {
    var instance: GreenBankMain? = null
    var economy: VaultEconomySupport? = null
    var mongoConnect: MongoDBDatabaseConnect? = null
    var configFile: YamlDocument? = null
    var databaseConfigFile: YamlDocument? = null
    var messagesConfigFile: YamlDocument? = null
    var isShutdown = false

    override fun onLoad() {
        try {
            configFile = Config(this).configFile()
            databaseConfigFile = DatabaseConfig(this).databaseConfig()
            messagesConfigFile = MessagesConfig(this).messagesConfig()
        } catch (error: IOException) { error.printStackTrace() }
        economy = VaultEconomySupport(this); mongoConnect = MongoDBDatabaseConnect(this)
    }

    override fun onEnable() {
        if (!setupEconomy()) fireCriticalError("Cannot find vault on your server, please download vault for functionality", "Please download vault:")
        if (mongoConnect!!.connection != null) {
            val connectDatabaseAsync: BukkitRunnable = object : BukkitRunnable() { override fun run() { mongoConnect!!.createConnection() } }
            when {
                databaseConfigFile!!.getBoolean("should-use-async-connection") -> connectDatabaseAsync.runTaskAsynchronously(this)
                !databaseConfigFile!!.getBoolean("should-use-async-connection") -> connectDatabaseAsync.runTask(this)
            }
        } else { fireCriticalError("Incorrect MongoDB Credentials", "Make sure your MongoDB URI, Database and Collection are correct in db.yml") }
        registerAll(this)
    }

    override fun onDisable(): Unit = try { initConfig() } catch (e: IOException) { throw RuntimeException(e) } finally { logger.info("[GreenBank] GreenBank has been successfully disabled!") }

    private fun fireCriticalError(message: String, error: String?) {
        logger.severe("---CRITICAL ERROR!---")
        logger.severe(message)
        logger.severe(" ")
        logger.severe("$error: Disabling Greenbank...")
        logger.severe("If you think this is a bug or glitch please contact the owner or staff of this plugin.")
        logger.severe("---------------------")
        isShutdown = true; server.pluginManager.disablePlugin(this)
    }

    @Throws(IOException::class) fun initConfig() {
        databaseConfigFile!!.save(); messagesConfigFile!!.save(); configFile!!.save()
        databaseConfigFile!!.reload(); messagesConfigFile!!.reload(); configFile!!.reload()
    }

    fun getConfigYaml(fileName: String?): InputStream = getResource(fileName!!) ?: throw NullPointerException("Filename of getConfigYaml() cannot be null")

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) return false
        Bukkit.getServicesManager().register(Economy::class.java, economy!!, this, ServicePriority.Highest)
        logger.info("Economy initialized and registered successfully.")
        return true
    }
}