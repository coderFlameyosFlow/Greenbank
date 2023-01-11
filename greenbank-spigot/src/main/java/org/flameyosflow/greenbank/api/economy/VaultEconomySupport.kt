package org.flameyosflow.greenbank.api.economy

import com.mongodb.client.model.Filters

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

import org.flameyosflow.greenbank.GreenBankMain
import org.flameyosflow.greenbank.database.mongodb.MongoDBDatabaseConnect
import org.flameyosflow.greenbank.utils.MessageUtils

import java.text.DecimalFormat
import java.util.*

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class VaultEconomySupport(private val greenBank: GreenBankMain) : Economy {
    private val databaseConnect: MongoDBDatabaseConnect = greenBank.mongoConnect!!

    /**
     * Checks if economy method is enabled.
     *
     * @return Success or Failure
     */
    override fun isEnabled(): Boolean = greenBank.isEnabled

    /**
     * Gets name of economy method
     *
     * @return Name of Economy Method
     */
    override fun getName(): String = "Greenbank Economy"

    /**
     * Returns true if the given implementation supports banks.
     *
     * @return true if the implementation supports banks
     */
    override fun hasBankSupport(): Boolean = false

    /**
     * Some economy plugins round off after a certain number of digits.
     * This function returns the number of digits the plugin keeps
     * or -1 if no rounding occurs.
     *
     * @return number of digits after the decimal point kept
     */
    override fun fractionalDigits(): Int = 0

    /**
     * Format amount into a human-readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param amount to format
     * @return Human-readable string describing amount
     */
    override fun format(amount: Double): String {
        if (amount < 1000) return String.format("%s", amount)
        val array = charArrayOf('K', 'M', 'B', 'T', 'P', 'E')
        var value = amount.toLong()
        var index = 0
        while (value / 1000 >= 1) {
            value /= 1000
            index++
        }
        val decimalFormat = DecimalFormat("#.###")
        return String.format("%s%s", decimalFormat.format(value), array[index])
    }

    /**
     * Returns the name of the currency in plural form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (plural)
     */
    override fun currencyNamePlural(): String? = null

    /**
     * Returns the name of the currency in singular form.
     * If the economy being used does not support currency names then an empty string will be returned.
     *
     * @return name of the currency (singular)
     */
    override fun currencyNameSingular(): String? = null

    /**
     * @param playerId The id of the player.
     */
    override fun hasAccount(playerId: String): Boolean = databaseConnect.collection.find(Filters.eq("uuid", playerId)).first() != null

    /**
     * Checks if this player has an account on the server yet
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param player to check
     * @return if the player has an account
     */
    override fun hasAccount(player: OfflinePlayer): Boolean = databaseConnect.collection.find(Filters.eq("uuid", player.uniqueId.toString())).first() != null

    /**
     * @param playerId The id of the player.
     * @param worldName name of the world
     */
    override fun hasAccount(playerId: String, worldName: String): Boolean = hasAccount(playerId)

    /**
     * Checks if this player has an account on the server yet on the given world
     * This will always return true if the player has joined the server at least once
     * as all major economy plugins auto-generate a player account when the player joins the server
     *
     * @param player    to check in the world
     * @param worldName world-specific account
     * @return if the player has an account
     */
    override fun hasAccount(player: OfflinePlayer, worldName: String): Boolean = hasAccount(player)

    /**
     * @param playerId The id of the player.
     */
    override fun getBalance(playerId: String): Double = if (hasAccount(playerId)) databaseConnect.getBalance(UUID.fromString(playerId)).toDouble() else 0.0

    /**
     * Gets balance of a player
     *
     * @param player of the player
     * @return Amount currently held in players account
     */
    override fun getBalance(player: OfflinePlayer): Double = if (hasAccount(player)) databaseConnect.getBalance(player.uniqueId).toDouble() else 0.0

    /**
     * @param playerId The id of the player.
     * @param world name of the world
     */
    override fun getBalance(playerId: String, world: String): Double = getBalance(playerId)

    /**
     * Gets balance of a player on the specified world.
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player to check
     * @param world  name of the world
     * @return Amount currently held in players account
     */
    override fun getBalance(player: OfflinePlayer, world: String): Double = getBalance(player)

    /**
     * @param playerId The id of the player.
     * @param amount amount to check for
     */
    override fun has(playerId: String, amount: Double): Boolean = (hasAccount(playerId)) && (getBalance(playerId) >= amount)

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to check
     * @param amount to check for
     * @return True if **player** has **amount**, False else wise
     */
    override fun has(player: OfflinePlayer, amount: Double): Boolean = (hasAccount(player)) && (getBalance(player) >= amount)

    /**
     * @param playerId The id of the player.
     * @param worldName name of the world
     * @param amount the amount to check
     */
    override fun has(playerId: String, worldName: String, amount: Double): Boolean = has(playerId, amount)

    /**
     * Checks if the player account has the amount in a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to check
     * @param worldName to check with
     * @param amount    to check for
     * @return True if **player** has **amount**, False else wise
     */
    override fun has(player: OfflinePlayer, worldName: String, amount: Double): Boolean = has(player, amount)

    /**
     * @param playerId The id of the player.
     * @param amount the amount to check
     */
    override fun withdrawPlayer(playerId: String, amount: Double): EconomyResponse {
        val playerTwo = Bukkit.getPlayer(UUID.fromString(playerId))
        val balance = databaseConnect.getBalance(playerTwo!!.uniqueId)
        return if (!hasAccount(playerId)) {
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("player-does-not-exist")))
        } else try {
            databaseConnect.setBalance((balance - amount).toLong(), playerTwo.uniqueId)
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("money-paid-success")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.SUCCESS, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("money-paid-success")))
        } catch (error: Exception) {
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
        }
    }

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to withdraw from
     * @param amount Amount to withdraw
     * @return Detailed response of transaction
     */
    override fun withdrawPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
        val playerTwo = player as Player
        val balance = databaseConnect.getBalance(playerTwo.uniqueId)
        return if (!hasAccount(player)) {
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("player-does-not-exist")))
        } else try {
            databaseConnect.setBalance((balance - amount).toLong(), playerTwo.uniqueId)
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("money-paid-success")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.SUCCESS, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("money-paid-success")))
        } catch (error: Exception) {
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
        }
    }

    /**
     * @param playerId The id of the player.
     * @param worldName the world name
     * @param amount the amount to check for
     */
    override fun withdrawPlayer(playerId: String, worldName: String, amount: Double): EconomyResponse = withdrawPlayer(playerId, amount)

    /**
     * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to withdraw from
     * @param worldName - name of the world
     * @param amount    Amount to withdraw
     * @return Detailed response of transaction
     */
    override fun withdrawPlayer(player: OfflinePlayer, worldName: String, amount: Double): EconomyResponse = withdrawPlayer(player, amount)

    /**
     * @param playerId The id of the player.
     * @param amount amount to deposit
     */
    override fun depositPlayer(playerId: String, amount: Double): EconomyResponse {
        val playerTwo = Bukkit.getPlayer(UUID.fromString(playerId))
        val balance = databaseConnect.getBalance(playerTwo!!.uniqueId)
        return if (!hasAccount(playerId)) {
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("player-does-not-exist")))
        } else try {
            databaseConnect.setBalance((balance + amount).toLong(), playerTwo.uniqueId)
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("received-money-success")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.SUCCESS, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("received-money-success")))
        } catch (error: Exception) {
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
        }
    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to deposit to
     * @param amount Amount to deposit
     * @return Detailed response of transaction
     */
    override fun depositPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
        val playerTwo = player as Player
        val balance = databaseConnect.getBalance(playerTwo.uniqueId)
        return if (!hasAccount(player)) {
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("player-does-not-exist")))
        } else try {
            databaseConnect.setBalance((balance + amount).toLong(), playerTwo.uniqueId)
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("received-money-success")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.SUCCESS, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("received-money-success")))
        } catch (error: Exception) {
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
            EconomyResponse(amount, balance.toDouble(), EconomyResponse.ResponseType.FAILURE, MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("not-enough-money")))
        }
    }

    /**
     * @param playerId The id of the player.
     * @param worldName the world
     * @param amount to deposit
     */
    override fun depositPlayer(playerId: String, worldName: String, amount: Double): EconomyResponse = depositPlayer(playerId, amount)

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the global balance will be returned.
     *
     * @param player    to deposit to
     * @param worldName name of the world
     * @param amount    Amount to deposit
     * @return Detailed response of transaction
     */
    override fun depositPlayer(player: OfflinePlayer, worldName: String, amount: Double): EconomyResponse = depositPlayer(player, amount)

    /**
     * @param name
     * @param player
     */
    override fun createBank(name: String, player: String): EconomyResponse? = null

    /**
     * Creates a bank account with the specified name and the player as the owner
     *
     * @param name   of account
     * @param player the account should be linked to
     * @return EconomyResponse Object
     */
    override fun createBank(name: String, player: OfflinePlayer): EconomyResponse? = null

    /**
     * Deletes a bank account with the specified name.
     *
     * @param name of the back to delete
     * @return if the operation completed successfully
     */
    override fun deleteBank(name: String): EconomyResponse? = null

    /**
     * Returns the amount the bank has
     *
     * @param name of the account
     * @return EconomyResponse Object
     */
    override fun bankBalance(name: String): EconomyResponse? = null

    /**
     * Returns true or false whether the bank has the amount specified - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to check for
     * @return EconomyResponse Object
     */
    override fun bankHas(name: String, amount: Double): EconomyResponse? = null

    /**
     * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to withdraw
     * @return EconomyResponse Object
     */
    override fun bankWithdraw(name: String, amount: Double): EconomyResponse? = null

    /**
     * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name   of the account
     * @param amount to deposit
     * @return EconomyResponse Object
     */
    override fun bankDeposit(name: String, amount: Double): EconomyResponse? = null
    /**
     * @param name
     * @param playerName
     */
    override fun isBankOwner(name: String, playerName: String): EconomyResponse? = null
    /**
     * Check if a player is the owner of a bank account
     *
     * @param name   of the account
     * @param player to check for ownership
     * @return EconomyResponse Object
     */
    override fun isBankOwner(name: String, player: OfflinePlayer): EconomyResponse? = null
    /**
     * @param name
     * @param playerName
     */
    override fun isBankMember(name: String, playerName: String): EconomyResponse? = null

    /**
     * Check if the player is a member of the bank account
     *
     * @param name   of the account
     * @param player to check membership
     * @return EconomyResponse Object
     */
    override fun isBankMember(name: String, player: OfflinePlayer): EconomyResponse? = null

    /**
     * Gets the list of banks
     *
     * @return the List of Banks
     */
    override fun getBanks(): List<String>? = null
    /**
     * @param playerId The id of the player.
     */
    override fun createPlayerAccount(playerId: String): Boolean = databaseConnect.addNewPlayer(Bukkit.getPlayer(UUID.fromString(playerId))!!)

    /**
     * Attempts to create a player account for the given player
     *
     * @param player OfflinePlayer
     * @return if the account creation was successful
     */
    override fun createPlayerAccount(player: OfflinePlayer): Boolean = databaseConnect.addNewPlayer((player as Player))

    /**
     * @param playerId
     * @param worldName
     */
    override fun createPlayerAccount(playerId: String, worldName: String): Boolean = createPlayerAccount(playerId)

    /**
     * Attempts to create a player account for the given player on the specified world
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this then false will always be returned.
     *
     * @param player    OfflinePlayer
     * @param worldName String name of the world
     * @return if the account creation was successful
     */
    override fun createPlayerAccount(player: OfflinePlayer, worldName: String): Boolean = createPlayerAccount(player)
}