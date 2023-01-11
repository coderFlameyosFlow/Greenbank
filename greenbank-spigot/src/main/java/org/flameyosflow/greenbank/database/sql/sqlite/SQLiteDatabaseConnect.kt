package org.flameyosflow.greenbank.database.sql.sqlite

import com.zaxxer.hikari.HikariConfig
import org.flameyosflow.greenbank.database.sql.SQLConnector
import java.sql.Connection
import java.sql.SQLException

@Suppress("SENSELESS_COMPARISON")
class SQLiteDatabaseConnect : SQLConnector() {
    /**
     * Creates a new config used to create the actual connection.
     *
     * @return the hikariConfig variable.
     */
    override fun createConfig(): HikariConfig {
        val config = HikariConfig()

        config.maximumPoolSize = 20
        config.poolName = "SQLite Greenbank Pool"
        config.jdbcUrl = connectionURL
        config.dataSourceClassName = "org.sqlite.SQLiteDataSource"
        config.driverClassName = "org.sqlite.JDBC"
        return config
    }

    override val connectionURL: String = ""

    /**
     * Creates a new connection to the database
     *
     * @return The connection to the database.
     */
    override fun getConnection(): Any = createConnection().connection
    private val connection = (getConnection() as Connection)

    override fun closeConnection() = if (connection != null && !connection.isClosed) try { connection.close() } catch (exception: SQLException) { exception.printStackTrace() } else {  }

    override fun edit(uuid: String?, value: Any?, identifier: String?) {
        TODO("Not yet implemented")
    }

    override fun delete(delete: Any?) {
        TODO("Not yet implemented")
    }
}