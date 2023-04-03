package com.greenbank.spigot.commands.manager;

import com.greenbank.api.IGreenbank;
import com.greenbank.api.settings.MessagesSettings;
import com.greenbank.api.settings.Settings;
import com.greenbank.spigot.hooks.vault.VaultEconomySupport;
import revxrsal.commands.annotation.Dependency;


public abstract class GreenbankCommand {
    @Dependency
    protected IGreenbank plugin;
    @Dependency
    protected MessagesSettings messages;
    @Dependency
    protected VaultEconomySupport economy;
    @Dependency
    protected Settings configSettings;
}
