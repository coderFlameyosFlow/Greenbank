package org.flameyosflow.greenbank

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

import org.flameyosflow.greenbank.commands.*
import org.flameyosflow.greenbank.listeners.*
import org.flameyosflow.greenbank.commands.noteconomy.GreenbankCommand

open class GreenBankSupport : JavaPlugin() {
    // get version from build.gradle.kts
    val version: String = "1.0.0 build 21"

    private fun registerCommand(name: String, command: GreenBankCommand?, greenBank: GreenBankMain) {
        val command2 = Bukkit.getPluginCommand(name) ?: return
        command2.setExecutor(command)

        // add command to internal register
        if (greenBank.configFile!!.getBoolean("override-all-other-plugins")) CommandSupport.add(name, command2)
    }
    private fun registerListener(listener: Listener, plugin: JavaPlugin) = server.pluginManager.registerEvents(listener, plugin)

    protected fun registerAll(greenBank: GreenBankMain) {
        registerCommand("balance", BalanceCommand(greenBank), greenBank); registerCommand("pay", PayCommand(greenBank), greenBank)
        registerCommand("eco", EcoCommand(greenBank), greenBank); registerCommand("greenbank", GreenbankCommand(greenBank), greenBank)
        registerListener(JoinListener(greenBank), greenBank as JavaPlugin)
    }
}