package com.br.infra.repository.user

import com.br.domain.services.database.DatabaseService
import com.br.domain.entity.Users
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.MongoException
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
        try {
            return usersCollection.insertOne(users).wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun findUserById(userId: String): Users? {
        try {
            val filter = Filters.eq("_id", ObjectId(userId))
            return usersCollection.find(filter).firstOrNull()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return null
    }

    override suspend fun findUsersByIds(usersIds: List<String>): List<Users> {
        try {
            val objectIds = usersIds.map { ObjectId(it) }
            return usersCollection.find(Filters.`in`("_id", objectIds)).toList()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return emptyList()
    }

    override suspend fun checkIfUserExists(email: String): Boolean {
        try {
            val count = usersCollection.countDocuments(Filters.eq(Users::email.name, email))
            return count > 0
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun checkIfUserExistsReturn(email: String): Users? {
        try {
            return usersCollection.find(Filters.eq(Users::email.name, email)).firstOrNull()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return null
    }
}