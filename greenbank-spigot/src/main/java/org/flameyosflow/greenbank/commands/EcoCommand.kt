package org.flameyosflow.greenbank.commands

import org.bukkit.Bukkit

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import org.flameyosflow.greenbank.GreenBankMain
import org.flameyosflow.greenbank.api.errors.CannotFindUserException
import org.flameyosflow.greenbank.utils.MessageUtils

/**
 * Administrative commands to handle some green money!
 *
 * @author FlameyosFlow
 * @since 1.0.0 BUILD 1
 */
class EcoCommand(private val greenBank: GreenBankMain) : GreenBankCommand(greenBank) {
    private val economy = greenBank.economy!!

    @Throws(CannotFindUserException::class)
    private fun givePlayer(sender: CommandSender, player: Player, amount: Long) {
        if (playerHasNotPlayedBefore(player)) throw CannotFindUserException(PLAYER_DOES_NOT_EXIST)
        economy.depositPlayer(player, amount.toDouble())
        sender.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("money-given-success")
                    .replace("%amount%", economy.format(amount.toDouble()))
                    .replace("%player%", player.name)))
        player.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("given-money-success")
                    .replace("%amount%", amount.toString())
                    .replace("%total%", (economy.getBalance(player) + amount).toString())))
    }

    @Throws(CannotFindUserException::class)
    private fun removePlayer(sender: CommandSender, player: Player, amount: Long) {
        if (playerHasNotPlayedBefore(player)) throw CannotFindUserException(PLAYER_DOES_NOT_EXIST)
        economy.withdrawPlayer(player, amount.toDouble())
        sender.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("removed-money-success")
                    .replace("%amount%", amount.toString())
                    .replace("%player%", player.name)))
        player.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("player-removed-money-success")
                    .replace("%amount%", amount.toString())
                    .replace("%total%", (economy.getBalance(player) - amount).toString())))
    }

    @Throws(CannotFindUserException::class)
    private fun setPlayer(sender: CommandSender, player: Player, amount: Long) {
        if (playerHasNotPlayedBefore(player)) throw CannotFindUserException(PLAYER_DOES_NOT_EXIST)
        greenBank.mongoConnect!!.setBalance(amount, player.uniqueId)
        sender.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("set-money-success")
                    .replace("%amount%", amount.toString())
                    .replace("%player%", player.name)))
        player.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("player-money-set-success")
                    .replace("%amount%", amount.toString())))
    }

    @Throws(CannotFindUserException::class)
    private fun resetPlayer(sender: CommandSender, player: Player) {
        if (playerHasNotPlayedBefore(player)) throw CannotFindUserException(PLAYER_DOES_NOT_EXIST)
        greenBank.mongoConnect!!.setBalance(greenBank.configFile!!.getLong("default-starting-balance"), player.uniqueId)
        sender.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("reset-money-success")))
        player.sendMessage(MessageUtils.colorMessage(greenBank.messagesConfigFile!!.getString("player-money-reset-success")))
    }

    override fun execute(sender: CommandSender, command: Command?, label: String?, args: Array<String>?): Boolean {
        // If the player doesn't have permission then forget this command ever happened.
        if (senderDoesNotHavePermission(sender, "greenbank.admin.eco")) {
            sender.sendMessage(MessageUtils.colorMessage(NOT_ENOUGH_PERMISSIONS))
            return false
        }
        if (args!!.size != 3) {
            sender.sendMessage(MessageUtils.colorMessage("&cUsage: /eco <command> <player> <amount>"))
            return false
        }
        val playerTwo = Bukkit.getPlayer(args[1])
        if (playerTwo == null) {
            sender.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
            return false
        }
        val amount = args[2].toLong()
        if (args[0].equals("give", ignoreCase = true) &&
            args[1].equals(playerTwo.name, ignoreCase = true) &&
            args[2].equals(amount.toString(), ignoreCase = true)
        ) {
            try {
                givePlayer(sender, playerTwo, amount)
            } catch (error: CannotFindUserException) {
                sender.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
            }
        } else if (args[0].equals("remove", ignoreCase = true) &&
                   args[1].equals(playerTwo.name, ignoreCase = true) &&
                   args[2].equals(amount.toString(), ignoreCase = true)) {
            try {
                removePlayer(sender, playerTwo, amount)
            } catch (error: CannotFindUserException) {
                sender.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
            }
        } else if (args[0].equals("set", ignoreCase = true) &&
                   args[1].equals(playerTwo.name, ignoreCase = true) &&
                   args[2].equals(amount.toString(), ignoreCase = true)) {
            try {
                setPlayer(sender, playerTwo, amount)
            } catch (error: CannotFindUserException) {
                sender.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
            }
        } else if (args[0].equals("reset", ignoreCase = true) &&
                   args[1].equals(playerTwo.name, ignoreCase = true) &&
                   args[2].equals(amount.toString(), ignoreCase = true)) {
            try {
                resetPlayer(sender, playerTwo)
            } catch (error: CannotFindUserException) {
                sender.sendMessage(MessageUtils.colorMessage(PLAYER_DOES_NOT_EXIST))
            }
        } else { sendEconomyHelpMessage(sender) }
        return true
    }
}