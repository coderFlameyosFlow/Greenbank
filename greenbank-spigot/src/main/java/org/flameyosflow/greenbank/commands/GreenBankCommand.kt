package org.flameyosflow.greenbank.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

import org.flameyosflow.greenbank.GreenBankMain
import org.flameyosflow.greenbank.utils.MessageUtils

import java.util.*

/**
 * DO NOT MIX THIS UP WITH Green***b***ankCommand.
 *
 * The main class for ALL greenbank commands, use this instead of CommandExecutor.
 * This is useful for much stuff, such as overriding commands, and comes with pre-made messages (from CommandSupport)
 *
 * @author FlameyosFlow
 */
@Suppress("LocalVariableName")
abstract class GreenBankCommand(greenBank: GreenBankMain) : CommandSupport(greenBank), CommandExecutor {
    abstract fun execute(sender: CommandSender, command: Command?, label: String?, args: Array<String>?): Boolean
    protected fun sendGreenbankHelpMessage(sender: CommandSender) {
        val GREENBANK_TOP_BOTTOM = "&7&l====================| &2&lGreen&a&lBank &e&lHelp &7&l|===================="
        sender.sendMessage(MessageUtils.colorMessage(GREENBANK_TOP_BOTTOM))
        sender.sendMessage(MessageUtils.colorMessage(" "))
        sender.sendMessage(MessageUtils.colorMessage("&7/greenbank &areload: "))
        sender.sendMessage(MessageUtils.colorMessage("&7Reloads ALL configs (messages, database and normal)."))
        sender.sendMessage(MessageUtils.colorMessage(" "))
        sender.sendMessage(MessageUtils.colorMessage(GREENBANK_TOP_BOTTOM))
    }

    protected fun sendEconomyHelpMessage(sender: CommandSender) {
        val ECONOMY_TOP_BOTTOM = "&7&l====================| &2&lEconomy &e&lHelp &7&l|===================="
        sender.sendMessage(MessageUtils.colorMessage(ECONOMY_TOP_BOTTOM))
        sender.sendMessage(MessageUtils.colorMessage(" "))
        sender.sendMessage(MessageUtils.colorMessage("&7/eco &agive <player> <amount>: "))
        sender.sendMessage(MessageUtils.colorMessage("&7Gives a player an amount of money."))
        sender.sendMessage(MessageUtils.colorMessage(" "))
        sender.sendMessage(MessageUtils.colorMessage("&7/eco &aremove <player> <amount>: "))
        sender.sendMessage(MessageUtils.colorMessage("&7Removes an amount of money from the player."))
        sender.sendMessage(MessageUtils.colorMessage(" "))
        sender.sendMessage(MessageUtils.colorMessage("&7/eco &aset <player> <amount>: "))
        sender.sendMessage(MessageUtils.colorMessage("&7Sets a player's balance to an amount of money."))
        sender.sendMessage(MessageUtils.colorMessage(" "))
        sender.sendMessage(MessageUtils.colorMessage("&7/eco &areset <player> <amount>: "))
        sender.sendMessage(MessageUtils.colorMessage("&7Resets a player's balance to the default starting balance."))
        sender.sendMessage(MessageUtils.colorMessage(" "))
        sender.sendMessage(MessageUtils.colorMessage(ECONOMY_TOP_BOTTOM))
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean = execute(sender, command, label, args)
}