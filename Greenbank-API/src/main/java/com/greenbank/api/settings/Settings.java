package com.greenbank.api.settings;

import com.greenbank.api.IGreenbank;
import dev.dejvokep.boostedyaml.YamlDocument;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Settings {
    @NotNull private final YamlDocument configFile;

    @Contract(pure = true)
    public Settings(IGreenbank greenBank) {
        configFile = greenBank.getConfigFile();
    }

    public double getDefaultStartingBalance() {
        return configFile.getDouble("default-starting-balance");
    }

    public boolean overrideAllOtherPlugins() {
        return configFile.getBoolean("currency.override-all-other-plugins");
    }

    public boolean shouldCreateAccountOnJoin() {
        return configFile.getBoolean("currency.create-account-on-join");
    }

    public String getCurrencySymbol() {
        return configFile.getString("currency.currency-symbol");
    }

    public String getCurrencyName() {
        return configFile.getString("currency.currency-name");
    }

    public String getCurrencyNamePlural() {
        return configFile.getString("currency.currency-name-plural");
    }

    public double getDefaultStartingBankBalance() {
        return configFile.getDouble("currency.default-starting-bank-balance");
    }
}
