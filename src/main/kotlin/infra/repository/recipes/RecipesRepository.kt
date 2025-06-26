package com.br.infra.repository.recipes

import com.br.application.payloads.requests.AddUpdateRecipesRequest
import com.br.domain.services.database.DatabaseService
import com.br.domain.entity.CategoryEnum
import com.br.domain.entity.Ingredients
import com.br.domain.entity.Recipes
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory

class RecipesRepository(
    databaseService: DatabaseService
): RecipesWriteOnlyRepository, RecipesReadOnlyRepository {

    private val logger = LoggerFactory.getLogger(RecipesRepository::class.java)
    private val recipesCollection = databaseService.database.getCollection<Recipes>(Constants.COLLECTION_NAME_RECIPES)

    override suspend fun create(recipes: Recipes): Boolean {
        return try {
            recipesCollection.insertOne(recipes).wasAcknowledged()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun remove(recipeId: String): Boolean {
        return try {
            recipesCollection.deleteOne(Filters.eq(Constants.MONGODB_ID, ObjectId(recipeId))).wasAcknowledged()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun update(
        recipeId: String,
        addUpdateRecipesRequest: AddUpdateRecipesRequest
    ): Boolean {
        return try {
            val update = recipesCollection.updateOne(
                filter = Filters.eq(Constants.MONGODB_ID, ObjectId(recipeId)),
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
            update.wasAcknowledged()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun getByUser(
        userId: String,
        categoryEnum: CategoryEnum?
    ): List<Recipes> {
        return try {
            val filters = mutableListOf(Filters.eq(Recipes::userId.name, userId))
            categoryEnum?.let { filters.add((Filters.eq(Recipes::category.name, it))) }
            recipesCollection.find(Filters.and(filters)).toList()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            emptyList()
        }
    }

    override suspend fun getById(recipeId: String, userId: String): Recipes? {
        return try {
            val recipesByUser = getByUser(userId, null)
            val recipe = recipesByUser.singleOrNull { it.id == recipeId }
            recipe
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            null
        }
    }

    override suspend fun search(
        nameOrIngredient: String,
        userId: String
    ): List<Recipes> {
        return try {
            val recipesUser = getByUser(userId, null).takeIf { recipes ->
                recipes.isNotEmpty()
            } ?: emptyList()

            recipesUser.filter { recipes ->
                nameOrIngredient.isBlank() || recipes.name.contains(nameOrIngredient, true) ||
                        recipes.ingredients.all { it.name.contains(nameOrIngredient, true) }
            }.sortedBy { recipes ->
                recipes.name
            }

        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            emptyList()
        }
    }

    override suspend fun checkIfExists(recipeId: String): Boolean {
        return try {
            val filters = Filters.eq(Constants.MONGODB_ID, ObjectId(recipeId))
            recipesCollection.find(filters).firstOrNull() != null
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }
}