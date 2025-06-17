package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("httpStatusCode")
    val httpStatusCode: Int,
    @SerializedName("message")
    val message: String
)