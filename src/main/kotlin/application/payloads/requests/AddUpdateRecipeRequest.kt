package com.br.application.payloads.requests

import com.google.gson.annotations.SerializedName

data class AddUpdateRecipeRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: Int,
    @SerializedName("preparationMode")
    val preparationMode: String,
    @SerializedName("preparationTime")
    val preparationTime: String,
    @SerializedName("ingredients")
    val ingredients: List<IngredientsRequest> = listOf(),
)
