package com.br.application.payloads.requests

import com.google.gson.annotations.SerializedName

data class AuthUserRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
