package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class SimpleResponse(
    @SerializedName("successful")
    val successful: Boolean,
    @SerializedName("message")
    val message: String
)