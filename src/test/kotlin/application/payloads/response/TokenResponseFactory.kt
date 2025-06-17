package com.br.application.payloads.response

import com.br.application.payloads.responses.TokenResponse

class TokenResponseFactory {

    fun create(isSuccessful: Boolean, message: String, token: String) : TokenResponse {
        return TokenResponse(isSuccessful = isSuccessful, message = message, token = token)
    }
}