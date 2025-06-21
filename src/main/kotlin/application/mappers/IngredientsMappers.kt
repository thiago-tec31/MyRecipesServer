package com.br.application.mappers

import com.br.application.payloads.requests.IngredientsRequest
import com.br.application.payloads.responses.IngredientsResponse
import com.br.domain.entity.Ingredients

fun IngredientsRequest.toIngredients(): Ingredients {
    return Ingredients(
        name = name,
        quantity = quantity,
        recipeId = ""
    )
}

fun Ingredients?.toIngredientsResponse(): IngredientsResponse {
    return IngredientsResponse(
        id = this?.id.orEmpty(),
        name = this?.name.orEmpty(),
        quantity = this?.quantity.orEmpty()
    )
}