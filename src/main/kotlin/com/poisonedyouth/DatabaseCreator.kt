package com.poisonedyouth

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.FileSystemResourceAccessor
import org.jetbrains.exposed.sql.Database
import java.io.File

object DatabaseCreator {

    fun init() {
        val dataSource = hikari()
        schemaMigration(dataSource)

        Database.connect(dataSource)
    }

    private fun schemaMigration(dataSource: HikariDataSource) {
        val database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(
                JdbcConnection(dataSource.connection)
            )
        val liquibase = Liquibase(
            "/changelog.sql",
            FileSystemResourceAccessor(File("src/main/resources")),
            database
        )
        liquibase.update("")
        liquibase.close()
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:file:./db"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.username = "root"
        config.password = "password"
        config.validate()
        return HikariDataSource(config)
    }
}