package org.flameyosflow.greenbank.config;

import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.flameyosflow.greenbank.GreenBankMain;

public class Config<T> {
    private final Logger logger;
    public Config(GreenBankMain plugin, Class<T> type)  {
        this.logger = plugin.getLogger();
    }
}
