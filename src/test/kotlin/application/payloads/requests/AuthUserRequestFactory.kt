package com.br.application.payloads.requests

class AuthUserRequestFactory {
    fun create(email: String, password: String) =
        AuthUserRequest(email, password)
}