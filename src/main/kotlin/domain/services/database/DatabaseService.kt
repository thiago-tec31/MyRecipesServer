package com.br.domain.services.database

import application.config.AppConfig
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.slf4j.LoggerFactory

class DatabaseService(
    private val appConfig: AppConfig
) {
    private val logger = LoggerFactory.getLogger(DatabaseService::class.java)

    val client = MongoClient.create(
        connectionString = appConfig.applicationConfig.property("database.mongodb_uri").getString(),
    )

    val database = client.getDatabase(
        databaseName = appConfig.applicationConfig.property("database.database_name").getString(),
    )

    init {
        logger.info("Database Service init")
        logger.info("Database Name: ${appConfig.applicationConfig.property("database.database_name").getString()}")
        logger.info("MongoDb URI: ${appConfig.applicationConfig.property("database.mongodb_uri").getString()}")
    }

    fun close() {
        client.close()
    }
}