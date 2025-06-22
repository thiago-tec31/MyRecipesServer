package com.br.domain.services.recipes

import com.br.application.payloads.requests.AddUpdateRecipesRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.domain.validations.AddValidationRecipeRequest
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class UpdateRecipeService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository,
    private val recipesWriteOnlyRepository: RecipesWriteOnlyRepository,
    private val validationRecipeRequest: AddValidationRecipeRequest
) {
    suspend fun update(
        userId: String,
        recipeId: String,
        addUpdateRecipesRequest: AddUpdateRecipesRequest
    ): SimpleResponse {
        val checkIfExists = recipesReadOnlyRepository.checkIfExists(recipeId)
        if (!checkIfExists) {
            return SimpleResponse(false, ErrorCodes.NO_RECIPE_FOUND.message)
        }

        val validator = validationRecipeRequest.validator(addUpdateRecipesRequest, userId)
        if (!validator.isSuccessful) {
            return validator
        }

        val updateRecipe = recipesWriteOnlyRepository.update(recipeId, addUpdateRecipesRequest)

        return if (updateRecipe) {
            SimpleResponse(isSuccessful = true, message = SuccessCodes.RECIPE_UPDATE_SUCCESS.message)
        } else {
            SimpleResponse(isSuccessful = false, message = ErrorCodes.RECIPE_UPDATE_ERROR.message)
        }
    }
}