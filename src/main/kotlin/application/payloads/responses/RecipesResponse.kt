package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("ownerName")
    val ownerName: String? = null,
    @SerializedName("totalIngredients")
    val totalIngredients: Int,
    @SerializedName("preparationTime")
    val preparationTime: String,
)
