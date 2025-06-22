package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("preparationTime")
    val preparationTime: String,
    @SerializedName("preparationMode")
    val preparationMode: String,
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("ingredients")
    val ingredients: List<IngredientsResponse>
)
