package com.br.application.payloads.requests

import com.google.gson.annotations.SerializedName

data class IngredientsRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: String
)
