package com.br.application.mappers

import com.br.application.payloads.responses.RecipeDetailResponse
import com.br.application.payloads.responses.RecipesResponse
import com.br.domain.entity.Recipes

fun Recipes.toRecipesResponse(): RecipesResponse {
    return RecipesResponse(
        id = this.id,
        name = this.name,
        category = this.category.description,
        totalIngredients = this.ingredients.size,
        preparationTime = this.preparationTime,
    )
}

fun Recipes?.toRecipeDetailResponse(): RecipeDetailResponse {
    return RecipeDetailResponse(
        id = this?.id.orEmpty(),
        name = this?.name.orEmpty(),
        category = this?.category?.description.orEmpty(),
        preparationMode = this?.preparationMode.orEmpty(),
        preparationTime = this?.preparationTime.orEmpty(),
        ingredients = this?.ingredients?.map {
            it.toIngredientsResponse()
        } ?: emptyList(),
        createAt = this?.createdAt.orEmpty(),
    )
}