package com.br.domain.services.recipes

import com.br.application.payloads.responses.SimpleResponse
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class DeleteRecipeService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository,
    private val recipesWriteOnlyRepository: RecipesWriteOnlyRepository
) {
    suspend fun delete(recipeId: String, userId: String): SimpleResponse {
        val checkIfExists = recipesReadOnlyRepository.checkIfExists(recipeId)
        if (!checkIfExists) {
            return SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.NO_RECIPE_FOUND.message
            )
        }

        val result = recipesWriteOnlyRepository.remove(recipeId)
        return if (result) {
            SimpleResponse(isSuccessful = true, message = SuccessCodes.RECIPE_DELETION_SUCCESS.message)
        } else {
            SimpleResponse(isSuccessful = false, message = ErrorCodes.RECIPE_DELETION_ERROR.message)
        }
    }
}