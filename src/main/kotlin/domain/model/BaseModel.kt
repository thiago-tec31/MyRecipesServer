package com.br.domain.model

import com.google.gson.annotations.SerializedName

abstract class BaseModel(
    @SerializedName("type")
    val type: String
)