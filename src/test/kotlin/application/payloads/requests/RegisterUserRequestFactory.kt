package com.br.application.payloads.requests

import application.payloads.requests.RegisterUserRequest

class RegisterUserRequestFactory {
    fun create(): RegisterUserRequest = RegisterUserRequest(
        name = "Alex",
        email = "alex@gmail.com",
        password = "alex123@",
        phone = "32 9 9140-3777"
    )
}