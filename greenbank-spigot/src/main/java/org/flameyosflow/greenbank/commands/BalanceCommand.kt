package org.flameyosflow.greenbank.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.flameyosflow.greenbank.GreenBankMain
import org.flameyosflow.greenbank.api.errors.CannotFindUserException
import org.flameyosflow.greenbank.utils.MessageUtils

/**
 * Check the amount of someone's green money!
 *
 * @author FlameyosFlow
 * @since 1.0.0 BUILD 1
 */
class BalanceCommand(private val greenBank: GreenBankMain) : GreenBankCommand(greenBank) {
    @Throws(CannotFindUserException::class)
    private fun getPlayerBalance(player: Player) {
        val economy = greenBank.economy!!
        if (playerHasNotPlayedBefore(player)) throw CannotFindUserException(PLAYER_DOES_NOT_EXIST)
        val balanceMessage = greenBank.messagesConfigFile!!.getString("other-player-balance")
            .replace("%balance%", economy.format(economy.getBalance(player)))
            .replace("%player%", player.name)
        player.sendMessage(MessageUtils.colorMessage(balanceMessage))
    }

    override fun execute(sender: CommandSender, command: Command?, label: String?, args: Array<String>?): Boolean {
        val player = sender as Player
        if (notPlayer(sender)) {
            sender.sendMessage(MessageUtils.colorMessage(ONLY_PLAYER))
            return false
        }

        if (playerDoesNotHavePermission(player, "greenbank.user.balance")) {
            player.sendMessage(MessageUtils.colorMessage(NOT_ENOUGH_PERMISSIONS))
            return false
        }

        if (args?.size == 1) {
            val player2 = Bukkit.getPlayer(args[0])!!
            try { getPlayerBalance(player2) } catch (error: CannotFindUserException) {
                player.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
                return false
            }
        } else {
            try { getPlayerBalance(player) } catch (error: CannotFindUserException) {
                /*
                getPlayerBalance of the sender shouldn't logically throw a CannotFindUserException unlike checking other player's balance
                although this is here just in case the balance was wrongly saved to the database or something.
                */
                player.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
                return false
            }
        }
        return true
    }
}