package org.flameyosflow.greenbank.config;

import org.flameyosflow.greenbank.GreenBankMain;

/**
 * Config object for YAML objects
 * @author tastybento
 * @since 1.0
 */
public interface ConfigObject {
    default GreenBankMain getPlugin() {
        return GreenBankMain.getPlugin();
    }

    /**
     * @return the uniqueId
     */
    default String getUniqueId() {
        return "config";
    }

    /**
     * @param uniqueId - unique ID the uniqueId to set
     */
    default void setUniqueId(String uniqueId) {}
}
