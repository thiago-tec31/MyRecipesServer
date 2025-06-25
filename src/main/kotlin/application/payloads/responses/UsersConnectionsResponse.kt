package com.br.application.payloads.responses

import com.br.domain.model.BaseModel
import com.br.util.Constants
import com.google.gson.annotations.SerializedName

data class UsersConnectionsResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("userToConnectId")
    val userToConnectId: String
) : BaseModel(Constants.TYPE_USERS_CONNECTIONS)
