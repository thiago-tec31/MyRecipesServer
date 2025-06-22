package com.br.domain.validations

import com.br.application.payloads.requests.AddUpdateRecipesRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

interface AddValidationRecipeRequest {
    suspend fun validator(request: AddUpdateRecipesRequest, userId: String?): SimpleResponse
}

class AddValidationRecipeRequestImpl : AddValidationRecipeRequest {
    override suspend fun validator(request: AddUpdateRecipesRequest, userId: String?): SimpleResponse {
        return when {
            userId.isNullOrEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.USER_ID_NOT_FOUND.message
            )

            request.name.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.TITLE_REQUIRED.message
            )

            request.category < 0 -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.CATEGORY_REQUIRED.message
            )

            request.preparationTime.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.PREPARATION_TIME_REQUIRED.message
            )

            request.preparationMode.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.PREPARATION_MODE_REQUIRED.message
            )

            request.ingredients.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.INGREDIENTS_REQUIRED.message
            )

            else -> SimpleResponse(
                isSuccessful = true,
                message = SuccessCodes.VALID_REGISTRATION.message
            )
        }
    }
}