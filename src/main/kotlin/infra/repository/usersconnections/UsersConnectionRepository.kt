package com.br.infra.repository.usersconnections

import com.br.domain.services.database.DatabaseService
import com.br.domain.entity.UsersConnections
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory

class UsersConnectionRepository(
    databaseService: DatabaseService
): UsersConnectionWriteOnlyRepository, UsersConnectionReadOnlyRepository {

    private val logger = LoggerFactory.getLogger(UsersConnectionRepository::class.java)
    private val usersConnectionsCollection = databaseService.database.getCollection<UsersConnections>(
        Constants.COLLECTION_NAME_USERS_CONNECTIONS)

    override suspend fun add(usersConnections: UsersConnections): Boolean {
        try {
            return usersConnectionsCollection.insertOne(usersConnections).wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun remove(userId: String, connectedWithUserId: String): Boolean {
        try {
            val deleteResult = usersConnectionsCollection.deleteMany(
                filter = Filters.or(
                    Filters.and(
                        Filters.eq(UsersConnections::userId.name, userId),
                        Filters.eq(UsersConnections::connectedWithUserId.name, connectedWithUserId)
                    ),
                    Filters.and(
                        Filters.eq(UsersConnections::connectedWithUserId.name, connectedWithUserId),
                        Filters.eq(UsersConnections::userId.name, userId)
                    )
                )
            )
            return deleteResult.wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun getUsersConnection(userId: String): List<UsersConnections> {
        try {
            return usersConnectionsCollection.find(
                Filters.eq(UsersConnections::userId.name, userId)
            ).toList()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return emptyList()
    }

    override suspend fun existingConnection(userId: String, connectedWithUserId: String): Boolean {
        try {
            val filter = Filters.or(
                Filters.and(
                    Filters.eq(UsersConnections::userId.name, userId),
                    Filters.eq(UsersConnections::connectedWithUserId.name, connectedWithUserId)
                ),
                Filters.and(
                    Filters.eq(UsersConnections::connectedWithUserId.name, connectedWithUserId),
                    Filters.eq(UsersConnections::userId.name, userId)
                )
            )
            return usersConnectionsCollection.find(filter).firstOrNull() != null
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }
}