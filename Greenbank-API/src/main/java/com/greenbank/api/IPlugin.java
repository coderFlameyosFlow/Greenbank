package com.greenbank.api;

public class IPlugin {
    private static IGreenbank plugin;

    public IPlugin(IGreenbank plugin) {
        IPlugin.plugin = plugin;
    }


    public static IGreenbank getPlugin() {
        return plugin;
    }
}
