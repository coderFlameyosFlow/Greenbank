/*
 * The balancetop command to show the people with the highest balances in the world or the server.
 *
 * This is not currently implemented, and I don't exactly know how to implement it
 * Although this is a base for when I figure out how to make this or someone else contributes.
 *
 * The file has no usages due to me not being able to make this.
 * Please consider finishing this if you know how to, or this will take a tad bit longer.
 */
package org.flameyosflow.greenbank.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.flameyosflow.greenbank.GreenBankMain

class BalanceTopCommand(greenBank: GreenBankMain) : GreenBankCommand(greenBank) {
    private val playersPerPage: Byte = 10
    override fun execute(sender: CommandSender, command: Command?, label: String?, args: Array<String>?): Boolean {
        // TODO: implement this command before doing the above.
        return false
    }
}