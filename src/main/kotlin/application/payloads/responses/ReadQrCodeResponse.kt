package com.br.application.payloads.responses

import com.google.gson.annotations.SerializedName

data class ReadQrCodeResponse(
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("isSuccessful")
    val isSuccessful: Boolean = false,
    @SerializedName("qrCodeCreatorId")
    val qrCodeCreatorId: String = "",
    @SerializedName("usersConnectionsResponse")
    val usersConnectionsResponse: UsersConnectionsResponse? = null
)
