package com.br.infra.repository.usersconnections

import com.br.domain.services.database.DatabaseService
import com.br.domain.entity.UsersConnections
import com.br.util.Constants
import com.br.util.ErrorCodes
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
        return try {
            usersConnectionsCollection.insertOne(usersConnections).wasAcknowledged()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun remove(userId: String, connectedWithUserId: String): Boolean {
        return try {
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
            deleteResult.wasAcknowledged()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun getUsersConnection(userId: String): List<UsersConnections> {
        return try {
            usersConnectionsCollection.find(
                Filters.eq(UsersConnections::userId.name, userId)
            ).toList()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            emptyList()
        }
    }

    override suspend fun existingConnection(userId: String, connectedWithUserId: String): Boolean {
        return try {
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
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }
}