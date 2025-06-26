package com.br.domain.services.users

import com.br.application.payloads.requests.RegisterUserRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.domain.entity.Users
import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.validations.AddValidationUserRequest
import com.br.infra.repository.users.UserReadOnlyRepository
import com.br.infra.repository.users.UserWriteOnlyRepository
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class RegisterUserService(
    private val addValidationUserRequest: AddValidationUserRequest,
    private val bCryptPasswordService: BCryptPasswordService,
    private val userWriteOnlyRepository: UserWriteOnlyRepository,
    private val userReadOnlyRepository: UserReadOnlyRepository
) {
    suspend fun register(registerUserRequest: RegisterUserRequest): SimpleResponse {

        val simpleResponse = addValidationUserRequest.validator(registerUserRequest)

        if (!simpleResponse.isSuccessful) {
            return simpleResponse
        }

        if (userReadOnlyRepository.checkIfUserExists(registerUserRequest.email)) {
            return SimpleResponse(isSuccessful = false, message = ErrorCodes.EMAIL_ALREADY_USED.message)
        }

        val hashedPassword = bCryptPasswordService.hashedPassword(
            const = Constants.COST_FACTOR,
            password = registerUserRequest.password
        )

        val users = Users(
            name = registerUserRequest.name,
            email = registerUserRequest.email,
            phone = registerUserRequest.phone,
            password = hashedPassword
        )

        val result = userWriteOnlyRepository.insertUser(users)
        return if (result) {
            SimpleResponse(isSuccessful = true, message = SuccessCodes.REGISTRATION_COMPLETED.message)
        } else {
            SimpleResponse(isSuccessful = false, message = ErrorCodes.REGISTRATION_ERROR.message)
        }
    }
}