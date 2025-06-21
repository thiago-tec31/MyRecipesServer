package com.br.domain.services.recipes

import com.br.application.mappers.toRecipeDetailResponse
import com.br.application.payloads.responses.RecipeDetailResponse
import com.br.infra.repository.recipes.RecipesReadOnlyRepository

class GetRecipeByIdService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository
) {
    suspend fun getRecipeById(recipeId: String, userId: String): RecipeDetailResponse? {
        return recipesReadOnlyRepository.getById(recipeId, userId)?.toRecipeDetailResponse()
    }
}