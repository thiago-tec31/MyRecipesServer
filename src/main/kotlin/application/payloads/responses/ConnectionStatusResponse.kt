package com.br.application.payloads.responses

import com.br.domain.model.BaseModel
import com.br.util.Constants
import com.google.gson.annotations.SerializedName

data class ConnectionStatusResponse(
    @SerializedName("connectionAccept")
    val connectionAccepted: Boolean,
    @SerializedName("message")
    val message: String
) : BaseModel(Constants.TYPE_CONNECTION_STATUS)
