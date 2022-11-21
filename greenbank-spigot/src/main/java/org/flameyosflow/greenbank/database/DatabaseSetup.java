package org.flameyosflow.greenbank.database;

import java.util.Arrays;
import java.util.Optional;

import org.flameyosflow.greenbank.GreenBankMain;

/**
 * @author Poslovitch, tastybento
 */
public interface DatabaseSetup {
    
    public static DatabaseType database;

    /**
     * Gets the type of database being used.
     * Currently supported options are MYSQL and SQLITE.
     * Default is SQLITE.
     * @return Database type
     */
    static Optional<Object> getDatabase() {
        GreenBankMain plugin = GreenBankMain.getPlugin();
        return Arrays.stream(DatabaseType.values())
                .filter(plugin.getSettings().getDatabaseType()::equals)
                .findFirst()
                .map(t -> DatabaseType.SQLITE);
    }
}
