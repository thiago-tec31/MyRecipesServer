package com.br.domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.application.payloads.responses.RecipesResponse
import com.br.domain.entity.CategoryEnum
import com.br.infra.repository.recipes.RecipesReadOnlyRepository

class GetUserRecipesService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository
) {
    suspend fun getAll(userId: String, category: Int?) : List<RecipesResponse> {
        val recipes = recipesReadOnlyRepository.getByUser(
            userId = userId,
            categoryEnum = category?.let { CategoryEnum.fromInt(it) }
        )
        return recipes.map { it.toRecipesResponse() }
    }
}