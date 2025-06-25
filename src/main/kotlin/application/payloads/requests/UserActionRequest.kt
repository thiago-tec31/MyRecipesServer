package com.br.application.payloads.requests

import com.br.domain.model.BaseModel
import com.br.util.Constants
import com.google.gson.annotations.SerializedName

data class UserActionRequest(
    @SerializedName("action")
    val action: Boolean,
    @SerializedName("userToConnectionId")
    val userToConnectionId: String? = null
): BaseModel(Constants.TYPE_USER_ACTION)
