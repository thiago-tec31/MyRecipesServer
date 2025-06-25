package com.br.application.payloads.responses

import com.br.domain.model.BaseModel
import com.br.util.Constants
import com.google.gson.annotations.SerializedName

data class ErrorMessageResponse(
    @SerializedName("message")
    val message: String
) : BaseModel(Constants.TYPE_ERROR_MESSAGE)
