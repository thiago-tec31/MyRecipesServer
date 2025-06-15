package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("isSuccessful")
    val isSuccessful: Boolean,
    @SerializedName("message")
    val message: String
)