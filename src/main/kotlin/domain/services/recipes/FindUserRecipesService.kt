package com.br.domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.application.payloads.responses.RecipesResponse
import com.br.infra.repository.recipes.RecipesReadOnlyRepository

class FindUserRecipesService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository
) {
    suspend fun search(nameOrIngredient: String, userId: String): List<RecipesResponse> {
        if (nameOrIngredient.isNotEmpty() || nameOrIngredient.isNotBlank()){
            val recipes = recipesReadOnlyRepository.search(nameOrIngredient, userId)
            return recipes.map { it.toRecipesResponse() }
        } else {
            return emptyList()
        }
    }
}