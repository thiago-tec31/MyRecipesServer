package com.br.application.payloads.response

import com.br.application.payloads.responses.SimpleResponse

class SimpleResponseFactory {
    fun create(isSuccessfully: Boolean, message: String) : SimpleResponse {
        return SimpleResponse(isSuccessfully, message)
    }
}