package org.flameyosflow.greenbank.database.sql.h2

import com.zaxxer.hikari.HikariConfig
import org.flameyosflow.greenbank.GreenBankMain
import org.flameyosflow.greenbank.database.sql.SQLConnector
import java.sql.Connection
import java.sql.SQLException

/**
 * The connection to the H2 database!
 *
 * @author FlameyosFlow, *insert your name/nickname here*
 */
class H2DatabaseConnect(private val greenBank: GreenBankMain, override val connectionURL: String?) : SQLConnector() {
    @Throws(SQLException::class)
    override fun getConnection(): Connection = dataSource!!.connection

    override fun closeConnection() = try { this.connection.close() } catch (error: SQLException) { greenBank.logger.info("Error while closing the database: " + error.message) }

    /**
     * @param uuid  h
     * @param value h
     * @param identifier h
     */
    override fun edit(uuid: String, value: Any, identifier: String) {}

    /**
     * @param delete h
     */
    override fun delete(delete: Any) {}

    override fun createConfig(): HikariConfig {
        val hikariConfig = HikariConfig()
        hikariConfig.poolName = "H2 GreenBank Database"
        hikariConfig.maximumPoolSize = 20
        hikariConfig.dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
        hikariConfig.driverClassName = "org.h2.Driver"
        hikariConfig.jdbcUrl = connectionURL
        return hikariConfig
    }
}