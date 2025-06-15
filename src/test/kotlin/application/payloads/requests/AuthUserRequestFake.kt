package com.br.application.payloads.requests

class AuthUserRequestFake {
    fun create(email: String, password: String) =
        AuthUserRequest(email, password)
}