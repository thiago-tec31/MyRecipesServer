package com.br.infra.repository.users

import com.br.domain.services.database.DatabaseService
import com.br.domain.entity.Users
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

class UserRepository(
    databaseService: DatabaseService
): UserWriteOnlyRepository, UserReadOnlyRepository {

    private val logger = LoggerFactory.getLogger(UserRepository::class.java)
    private val usersCollection = databaseService.database.getCollection<Users>(Constants.COLLECTION_NAME_USERS)

    override suspend fun insertUser(users: Users): Boolean {
        return try {
            usersCollection.insertOne(users).wasAcknowledged()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun findUserById(userId: String): Users? {
        return try {
            val filter = Filters.eq(Constants.MONGODB_ID, ObjectId(userId))
            usersCollection.find(filter).firstOrNull()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            null
        }
    }

    override suspend fun findUsersByIds(usersIds: List<String>): List<Users> {
        return try {
            val objectIds = usersIds.map { ObjectId(it) }
            usersCollection.find(Filters.`in`(Constants.MONGODB_ID, objectIds)).toList()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            emptyList()
        }
    }

    override suspend fun checkIfUserExists(email: String): Boolean {
        return try {
            val filters = Filters.eq(Users::email.name, email)
            usersCollection.find(filters).firstOrNull() != null
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun checkIfUserExistsReturn(email: String): Users? {
        return try {
            usersCollection.find(Filters.eq(Users::email.name, email)).firstOrNull()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            null
        }
    }
}