package org.flameyosflow.greenbank.database.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import org.bukkit.Bukkit

import org.flameyosflow.greenbank.database.DatabaseConnector

import java.sql.Connection
import java.sql.SQLException

abstract class SQLConnector : DatabaseConnector<HikariDataSource> {
    protected var dataSource: HikariDataSource? = null

    /**
     * Creates a new config used to create the actual connection.
     *
     * @return the hikariConfig variable.
     */
    abstract fun createConfig(): HikariConfig?

    override fun createConnection(): HikariDataSource {
        // if dataSource already exists, do nothing and just return the dataSource.
        if (dataSource != null) return dataSource as HikariDataSource

        // This code is executed if the dataSource is actually null, and WE DO NOT WANT THAT!
        try {
            dataSource = HikariDataSource(createConfig())
            (connection as Connection).use { connection -> connection.isValid(5 * 1000) }
        } catch (e: SQLException) {
            Bukkit.getLogger().severe("Could not connect to the database! " + e.message)
            e.printStackTrace()
            dataSource = null
        }
        return dataSource!!
    }

    abstract val connectionURL: String?
}