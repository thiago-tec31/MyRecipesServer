package com.br.application.payloads.requests

import application.payloads.requests.AddUserRequest

class AddUserRequestFactory {
    fun create(): AddUserRequest = AddUserRequest(
        name = "Alex",
        email = "alex@gmail.com",
        password = "alex123@",
        phone = "32 9 9140-3777"
    )
}