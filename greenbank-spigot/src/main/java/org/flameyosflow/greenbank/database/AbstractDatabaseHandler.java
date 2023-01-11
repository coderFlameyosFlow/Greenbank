package org.flameyosflow.greenbank.database;

import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.database.mongodb.MongoDBDatabaseConnect;
import org.flameyosflow.greenbank.database.sql.sqlite.SQLiteDatabaseConnect;

import java.util.Objects;

public class AbstractDatabaseHandler {
    GreenBankMain greenBank = new GreenBankMain().getInstance();
    public Object getDatabase() {
        String databaseType = Objects.requireNonNull(greenBank.getDatabaseConfigFile()).getString("database-type");
        switch (databaseType) {
            case "SQLite": new SQLiteDatabaseConnect();
            case "MongoDB": new MongoDBDatabaseConnect(greenBank);
        }
        return databaseType;
    }
}
