package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class IngredientsResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: String,
)
