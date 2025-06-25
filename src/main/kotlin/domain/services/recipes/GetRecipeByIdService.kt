package com.br.domain.services.recipes

import com.br.application.mappers.toRecipeDetailResponse
import com.br.application.payloads.responses.RecipeDetailResponse
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository

class GetRecipeByIdService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository,
    private val usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository
) {
    suspend fun getRecipeById(recipeId: String, userId: String): RecipeDetailResponse? {
        val recipeModel = recipesReadOnlyRepository.getById(recipeId, userId)
        if (recipeModel != null) {
            return recipeModel.toRecipeDetailResponse()
        }

        val usersConnections = usersConnectionReadOnlyRepository.getUsersConnection(userId)

        val sharedRecipes = usersConnections.firstNotNullOfOrNull { usersConnection ->
            recipesReadOnlyRepository.getById(recipeId, usersConnection.connectedWithUserId)
        }

        return sharedRecipes?.toRecipeDetailResponse()
    }
}