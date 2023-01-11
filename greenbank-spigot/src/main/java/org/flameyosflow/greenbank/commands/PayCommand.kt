package org.flameyosflow.greenbank.commands

import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.flameyosflow.greenbank.GreenBankMain
import org.flameyosflow.greenbank.api.errors.CannotFindUserException
import org.flameyosflow.greenbank.api.errors.CannotPerformTransactionOnSelf
import org.flameyosflow.greenbank.api.errors.NotEnoughMoney
import org.flameyosflow.greenbank.utils.MessageUtils
import org.junit.Assert

/**
 * Pay someone some green money!
 *
 * @author FlameyosFlow
 * @since 1.0.0 BUILD 1
 */
class PayCommand(private val greenBank: GreenBankMain) : GreenBankCommand(greenBank) {
    private val economy = greenBank.economy!!
    @Throws(NotEnoughMoney::class, CannotFindUserException::class, CannotPerformTransactionOnSelf::class)
    private fun payPlayer(player: Player, playerTwo: Player, amount: Long) {
        if (playerHasNotPlayedBefore(playerTwo)) throw CannotFindUserException(PLAYER_DOES_NOT_EXIST)
        if (!economy.has(player, amount.toDouble())) throw NotEnoughMoney(NOT_ENOUGH_MONEY)
        if (player.uniqueId === playerTwo.uniqueId) throw CannotPerformTransactionOnSelf("Player disallowed to pay themself!")

        val economyResponse: EconomyResponse = economy.withdrawPlayer(player, amount.toDouble())
        if (economyResponse.transactionSuccess()) player.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("money-paid-success")
                    .replace("%amount%", economy.format(amount.toDouble()))
                    .replace("%player%", playerTwo.name)
                    .replace("%total%", economy.format(economy.getBalance(player) - amount))))
        val anotherEconomyResponse: EconomyResponse = economy.depositPlayer(playerTwo, amount.toDouble())
        if (anotherEconomyResponse.transactionSuccess()) player.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("received-money-success")
                    .replace("%amount%", economy.format(amount.toDouble()))
                    .replace("%player%", player.name)
                    .replace("%total%", economy.format(economy.getBalance(playerTwo) + amount))))
    }

    override fun execute(sender: CommandSender, command: Command?, label: String?, args: Array<String>?): Boolean {
        val player = sender as Player
        if (notPlayer(sender)) {
            sender.sendMessage(ONLY_PLAYER)
            return false
        }
        if (playerDoesNotHavePermission(player, "greenbank.user.pay")) {
            sender.sendMessage(NOT_ENOUGH_PERMISSIONS)
            return false
        }
        if (args?.size != 2) {
            player.sendMessage(MessageUtils.colorMessage("&cUsage: /pay <player> <amount>"))
            return false
        }
        val amount: Long?
        try { amount = args[0].toLong() } catch (e: Exception) {
            player.sendMessage(MessageUtils.colorMessage("&cExpected number for <amount> (usage: /pay <player> <amount>), but found \"${(args[1])}\"."))
            return false
        }
        val playerTwo = Bukkit.getPlayer(args[1]); Assert.assertNotNull(PLAYER_DOES_NOT_EXIST, playerTwo)
        try {
            payPlayer(player, playerTwo!!, amount)
            playerTwo.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("received-money-success")
                .replace("%amount%", amount.toString())
                .replace("%player%", player.name)
                .replace("%total%", economy.getBalance(playerTwo).toString())))
        } catch (error: CannotFindUserException) {
            player.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
            return false
        } catch (error: NotEnoughMoney) {
            player.sendMessage(MessageUtils.colorMessage(NOT_ENOUGH_MONEY))
            return false
        } catch (error: CannotPerformTransactionOnSelf) {
            player.sendMessage(MessageUtils.colorMessage("&cCannot perform transaction on yourself, please choose someone."))
            return false
        }
        return true
    }
}