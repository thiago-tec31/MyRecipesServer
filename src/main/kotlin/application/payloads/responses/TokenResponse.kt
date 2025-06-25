package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("isSuccessful")
    val isSuccessful: Boolean,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("userName")
    val userName: String? = null
)
