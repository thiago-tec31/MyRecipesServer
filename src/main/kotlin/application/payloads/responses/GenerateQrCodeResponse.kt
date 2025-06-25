package com.br.application.payloads.responses

import com.br.domain.model.BaseModel
import com.br.util.Constants
import com.google.gson.annotations.SerializedName

data class GenerateQrCodeResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("qrCodeBase64")
    val qrCodeBase64: String
): BaseModel(Constants.TYPE_QRCODE_GENERATED)