package com.br.infra.repository.recipe

import com.br.domain.entity.CategoryEnum
import com.br.domain.entity.Recipes

interface RecipesReadOnlyRepository {
    suspend fun getByUser(userId: String, categoryEnum: CategoryEnum?): List<Recipes>
    suspend fun getById(recipeId: String, userId: String): Recipes?
    suspend fun search(nameOrIngredient: String, userId: String): List<Recipes>
    suspend fun checkIfExists(recipeId: String): Boolean
}