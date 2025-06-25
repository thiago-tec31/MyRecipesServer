package com.br.application.payloads.responses

import com.br.domain.model.BaseModel
import com.br.util.Constants
import com.google.gson.annotations.SerializedName

data class TimeRemainingResponse(
    @SerializedName("duration")
    val duration: Int,
): BaseModel(Constants.TYPE_TIME_REMAINING)