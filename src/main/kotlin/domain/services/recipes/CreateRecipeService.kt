package com.br.domain.services.recipes

import com.br.application.mappers.toIngredients
import com.br.application.payloads.requests.AddUpdateRecipesRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.domain.entity.CategoryEnum
import com.br.domain.entity.Recipes
import com.br.domain.validations.AddValidationRecipeRequest
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import org.bson.types.ObjectId

class CreateRecipeService(
    private val addValidationRecipeRequest: AddValidationRecipeRequest,
    private val recipesWriteOnlyRepository: RecipesWriteOnlyRepository
) {
    suspend fun createRecipe(request: AddUpdateRecipesRequest, userId: String) : SimpleResponse {
        val validator = addValidationRecipeRequest.validator(request, userId)
        if (!validator.isSuccessful) {
            return validator
        }

        val recipeId = ObjectId.get().toHexString()

        val recipe = Recipes(
            id = recipeId,
            name = request.name,
            userId = userId,
            category = CategoryEnum.fromInt(request.category),
            preparationMode = request.preparationMode,
            preparationTime = request.preparationTime,
            ingredients = request.ingredients.map { it.toIngredients().copy(recipeId = recipeId) }
        )

        val result = recipesWriteOnlyRepository.create(recipe)

        return if (result) {
            SimpleResponse(isSuccessful = true, message = SuccessCodes.RECIPE_REGISTRATION_SUCCESS.message)
        } else {
            SimpleResponse(isSuccessful = false, message = ErrorCodes.RECIPE_REGISTRATION_ERROR.message)
        }
    }
}