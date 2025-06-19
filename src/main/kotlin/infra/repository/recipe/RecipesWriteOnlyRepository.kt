package com.br.infra.repository.recipe

import com.br.application.payloads.requests.AddUpdateRecipeRequest
import com.br.domain.entity.Recipes

interface RecipesWriteOnlyRepository {
    suspend fun create(recipes: Recipes): Boolean
    suspend fun remove(recipeId: String): Boolean
    suspend fun update(recipeId: String, addUpdateRecipeRequest: AddUpdateRecipeRequest): Boolean
}