package com.br.infra.repository.recipes

import com.br.application.payloads.requests.AddUpdateRecipesRequest
import com.br.domain.entity.Recipes

interface RecipesWriteOnlyRepository {
    suspend fun create(recipes: Recipes): Boolean
    suspend fun remove(recipeId: String): Boolean
    suspend fun update(recipeId: String, addUpdateRecipesRequest: AddUpdateRecipesRequest): Boolean
}