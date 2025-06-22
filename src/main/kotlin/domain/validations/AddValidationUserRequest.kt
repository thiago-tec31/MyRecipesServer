package com.br.domain.validations

import com.br.application.payloads.requests.RegisterUserRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

interface AddValidationUserRequest {
    suspend fun validator(request: RegisterUserRequest) : SimpleResponse
}

class AddValidationUserRequestImpl : AddValidationUserRequest  {

    private val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    private val phoneRegex = "[0-9]{2} [1-9]{1} [0-9]{4}-[0-9]{4}".toRegex()


    override suspend fun validator(request: RegisterUserRequest): SimpleResponse {
        return when {
            request.name.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.NAME_REQUIRED.message
            )

            request.email.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                ErrorCodes.EMAIL_REQUIRED.message
            )

            !emailRegex.matches(request.email) -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.INVALID_EMAIL.message
            )

            request.password.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.PASSWORD_REQUIRED.message
            )

            request.password.length < 6 -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.PASSWORD_TOO_SHORT.message
            )

            request.phone.isEmpty() -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.PHONE_REQUIRED.message
            )

            !phoneRegex.matches(request.phone) -> SimpleResponse(
                isSuccessful = false,
                message = ErrorCodes.INVALID_PHONE_FORMAT.message
            )

            else -> SimpleResponse(
                isSuccessful = true,
                message = SuccessCodes.VALID_REGISTRATION.message
            )
        }
    }
}