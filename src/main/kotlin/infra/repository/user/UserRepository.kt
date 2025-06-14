package com.br.infra.repository.user

import com.br.domain.entity.User
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

class UserRepository(
    mongoDatabase: MongoDatabase
): UserWriteOnlyRepository, UserReadOnlyRepository {

    private val logger = LoggerFactory.getLogger(UserRepository::class.java)
    private val usersCollection = mongoDatabase.getCollection<User>(Constants.COLLECTION_NAME_USERS)

    override suspend fun insertUser(user: User): Boolean {
        try {
            return usersCollection.insertOne(user).wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun findUserById(userId: String): User? {
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

    override suspend fun findUsersByIds(usersIds: List<String>): List<User> {
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
            val count = usersCollection.countDocuments(Filters.eq(User::email.name, email))
            return count > 0
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun checkIfUserExistsReturn(email: String): User? {
        try {
            return usersCollection.find(Filters.eq(User::email.name, email)).firstOrNull()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return null
    }
}