package application.payloads.requests

import com.google.gson.annotations.SerializedName

data class AddUserRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String
)
