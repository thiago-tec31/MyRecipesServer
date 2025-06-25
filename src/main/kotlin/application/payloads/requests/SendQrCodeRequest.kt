package com.br.application.payloads.requests

import com.br.domain.model.BaseModel
import com.br.util.Constants
import com.google.gson.annotations.SerializedName

data class SendQrCodeRequest(
    @SerializedName("qrcode")
    val qrcode: String
): BaseModel(Constants.TYPE_SEND_QRCODE)
