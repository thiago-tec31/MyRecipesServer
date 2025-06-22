package com.br.infra.repository.recipes

import com.br.application.payloads.requests.AddUpdateRecipesRequest
import com.br.domain.database.DatabaseService
import com.br.domain.entity.CategoryEnum
import com.br.domain.entity.Ingredients
import com.br.domain.entity.Recipes
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

class RecipesRepository(
    private val databaseService: DatabaseService
): RecipesWriteOnlyRepository, RecipesReadOnlyRepository {

    private val logger = LoggerFactory.getLogger(RecipesRepository::class.java)
    private val recipesCollection = databaseService.database.getCollection<Recipes>(Constants.COLLECTION_NAME_RECIPES)

    override suspend fun create(recipes: Recipes): Boolean {
        try {
            return recipesCollection.insertOne(recipes).wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun remove(recipeId: String): Boolean {
        try {
            return recipesCollection.deleteOne(Filters.eq("_id", ObjectId(recipeId))).wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun update(
        recipeId: String,
        addUpdateRecipesRequest: AddUpdateRecipesRequest
    ): Boolean {
        try {
            val update = recipesCollection.updateOne(
                filter = Filters.eq("_id", ObjectId(recipeId)),
                update = Updates.combine(
                    Updates.set(Recipes::name.name, addUpdateRecipesRequest.name),
                    Updates.set(Recipes::category.name, CategoryEnum.fromInt(addUpdateRecipesRequest.category)),
                    Updates.set(Recipes::preparationMode.name, addUpdateRecipesRequest.preparationMode),
                    Updates.set(Recipes::preparationTime.name, addUpdateRecipesRequest.preparationTime),
                    Updates.set(Recipes::ingredients.name, addUpdateRecipesRequest.ingredients.map { ingredientsRequest ->
                        Ingredients(
                            recipeId = recipeId,
                            name = ingredientsRequest.name,
                            quantity = ingredientsRequest.quantity
                        )
                    }),
                )
            )
            return update.wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun getByUser(
        userId: String,
        categoryEnum: CategoryEnum?
    ): List<Recipes> {
        try {
            val filters = mutableListOf(Filters.eq(Recipes::userId.name, userId))
            categoryEnum?.let { filters.add((Filters.eq(Recipes::category.name, it))) }
            return recipesCollection.find(Filters.and(filters)).toList()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return emptyList()
    }

    override suspend fun getById(recipeId: String, userId: String): Recipes? {
        try {
            val recipesByUser = getByUser(userId, null)
            val recipe = recipesByUser.singleOrNull { it.id == recipeId }
            return recipe
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return null
    }

    override suspend fun search(
        nameOrIngredient: String,
        userId: String
    ): List<Recipes> {
        try {
            val recipesUser = getByUser(userId, null).takeIf { recipes ->
                recipes.isNotEmpty()
            } ?: return emptyList()

            return recipesUser.filter { recipes ->
                nameOrIngredient.isBlank() || recipes.name.contains(nameOrIngredient, true) ||
                        recipes.ingredients.all { it.name.contains(nameOrIngredient, true) }
            }.sortedBy { recipes ->
                recipes.name
            }

        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return emptyList()
    }

    override suspend fun checkIfExists(recipeId: String): Boolean {
        val count = recipesCollection.countDocuments(Filters.eq("_id", ObjectId(recipeId)))
        return count > 0
    }
}