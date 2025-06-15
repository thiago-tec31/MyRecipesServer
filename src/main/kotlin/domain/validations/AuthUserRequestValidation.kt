package com.br.domain.validations

import com.br.application.payloads.requests.AuthUserRequest
import com.br.application.payloads.responses.TokenResponse
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

interface AuthUserRequestValidation {
    suspend fun validator(request: AuthUserRequest): TokenResponse
}

class AuthUserRequestValidationImpl : AuthUserRequestValidation {

    private val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()

    override suspend fun validator(request: AuthUserRequest): TokenResponse {
        return when {
            request.email.isEmpty() -> TokenResponse(
                isSuccessful = false,
                message = ErrorCodes.EMAIL_REQUIRED.message
            )

            !emailRegex.matches(request.email) -> TokenResponse(
                isSuccessful = false,
                message = ErrorCodes.INVALID_EMAIL.message
            )

            request.password.isEmpty() -> TokenResponse(
                isSuccessful = false,
                message = ErrorCodes.PASSWORD_REQUIRED.message
            )

            request.password.length < 6 -> TokenResponse(
                isSuccessful = false,
                message = ErrorCodes.PASSWORD_TOO_SHORT.message
            )

            else -> TokenResponse(
                isSuccessful = true,
                message = SuccessCodes.VALID_REGISTRATION.message
            )
        }

    }
}