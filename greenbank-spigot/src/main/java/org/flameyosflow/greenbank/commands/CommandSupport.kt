package org.flameyosflow.greenbank.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.SimpleCommandMap
import org.bukkit.entity.Player

import org.flameyosflow.greenbank.GreenBankMain

/*
 * Adds response for certain checks, so this class
 * has all the variables to give us the check responses
 * and add simplicity to add/edit/remove the responses
 *
 * or add methods for some type of support.
 *
 * Added since 1.0.0 build 1
 */
@Suppress("UNCHECKED_CAST", "PropertyName")
open class CommandSupport(greenBank: GreenBankMain) {
    @JvmField protected val NOT_ENOUGH_PERMISSIONS: String = greenBank.messagesConfigFile!!.getString("not-enough-permissions")
    @JvmField protected val ONLY_PLAYER: String = greenBank.messagesConfigFile!!.getString("only-player-command")
    @JvmField protected val PLAYER_DOES_NOT_EXIST: String = greenBank.messagesConfigFile!!.getString("player-does-not-exist")
    @JvmField protected val NOT_ENOUGH_MONEY: String = greenBank.messagesConfigFile!!.getString("not-enough-money")
    companion object {
        @JvmStatic protected fun notPlayer(sender: CommandSender?): Boolean = sender !is Player


        /**
         * Checks if the Player has the permission, If the command is for ONLY player, use this.
         *
         * @return true if the Player has the permission, otherwise false.
         *
         *
         * Added since 1.0.0 build 1
         */
        @JvmStatic protected fun playerDoesNotHavePermission(player: Player, permission: String): Boolean = !player.hasPermission(permission)

        /**
         * Checks if the CommandSender has the permission, If the command is for console AND player, use this.
         *
         * @return true if the CommandSender has the permission, otherwise false.
         *
         *
         * Added since 1.0.0 build 1
         */

        @JvmStatic protected fun senderDoesNotHavePermission(sender: CommandSender, permission: String): Boolean = !sender.hasPermission(permission)

        @JvmStatic protected fun playerHasNotPlayedBefore(player: Player): Boolean = !player.hasPlayedBefore()

        /**
         * Retrieves the current command map instance
         *
         * @return the command map instance
         */
        private fun retrieveMap(): SimpleCommandMap? {
            return try {
                val field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
                field.isAccessible = true
                field[Bukkit.getServer()] as SimpleCommandMap
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }

        /**
         * Adds a command to the Command Map
         *
         * @param alias   The alias
         * @param command The command instance
         */
        @JvmStatic fun add(alias: String, command: Command) {
            try {
                val field = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
                field.isAccessible = true
                val map: CommandMap? = retrieveMap()
                val knownCommands = field[map] as MutableMap<String, Command>
                knownCommands["cf:$alias"] = command
                knownCommands[alias] = command
                field[map] = knownCommands
            } catch (ex: NoSuchFieldException) {
                Bukkit.getLogger().severe("knownCommands field not found for registry")
                Bukkit.getLogger().severe("update your server or switch to a supported server platform")
                Bukkit.getLogger().severe("$ex: ${ex.message}")
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }
}