package org.flameyosflow.greenbank;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import org.flameyosflow.greenbank.commands.GreenBankCommand;

import org.jetbrains.annotations.NotNull;

public class GreenBankSupport extends JavaPlugin {

    /*
     * getVersion() returns the version of the plugin.
     *
     * (x is a number)
     * x.x.x is the initial version of the plugin
     * build x says how many times the provided initial version has been tested
     *
     * for example: 1.0.0 build 6 (1.0.0 was tested 6 times)
     * another example: 1.0.1 build 4 (1.0.1 was tested 4 times)
     *
     * every little different version has different number of builds.
     */
    public String getVersion() {
        return "1.0.0 BUILD 10";
    }

    protected void registerCommand(String name, GreenBankCommand command) {
        GreenBankCommand.register(name, command);
    }

    protected void registerListener(@NotNull Listener listener, @NotNull JavaPlugin plugin) {
        getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
