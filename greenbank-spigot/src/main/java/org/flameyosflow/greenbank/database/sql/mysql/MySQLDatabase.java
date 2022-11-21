package org.flameyosflow.greenbank.database.sql.mysql;


import org.flameyosflow.greenbank.GreenBankMain;
import org.flameyosflow.greenbank.database.AbsDatabaseHandler;
import org.flameyosflow.greenbank.database.DatabaseConnectionSettingsImpl;
import org.flameyosflow.greenbank.database.DatabaseSetup;

public class MySQLDatabase implements DatabaseSetup {
    /**
     * MySQL Database Connector
     */
    private MySQLConnect connector;


    /*
     * {@inheritDoc}
     */
    @Override
    public <T> AbsDatabaseHandler<T> getHandler(Class<T> type)
    {
        GreenBankMain plugin = GreenBankMain.getPlugin();

        if (this.connector == null)
        {
            this.connector = new MySQLConnect(new DatabaseConnectionSettingsImpl(
                plugin.getSettings().getDatabaseHost(),
                plugin.getSettings().getDatabasePort(),
                plugin.getSettings().getDatabaseName(),
                plugin.getSettings().getDatabaseUsername(),
                plugin.getSettings().getDatabasePassword(),
                plugin.getSettings().isUseSSL(),
                plugin.getSettings().getMaximumPoolSize()));
        }

        return new <T> AbsDatabaseHandler<T>(plugin, type, this.connector);
    }
}
