package com.br.domain.services.user

import application.payloads.request.AddUserRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.domain.entity.User
import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.validations.AddValidationUserRequest
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.infra.repository.user.UserWriteOnlyRepository
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class AddUserService(
    private val addValidationUserRequest: AddValidationUserRequest,
    private val bCryptPasswordService: BCryptPasswordService,
    private val userWriteOnlyRepository: UserWriteOnlyRepository,
    private val userReadOnlyRepository: UserReadOnlyRepository
) {
    suspend fun addUser(addUserRequest: AddUserRequest): SimpleResponse {

        val simpleResponse = addValidationUserRequest.validator(addUserRequest)

        if (!simpleResponse.isSuccessful) {
            return simpleResponse
        }

        if (userReadOnlyRepository.checkIfUserExists(addUserRequest.email)) {
            return SimpleResponse(isSuccessful = false, message = ErrorCodes.EMAIL_ALREADY_USED.message)
        }

        val hashedPassword = bCryptPasswordService.hashedPassword(
            const = Constants.COST_FACTOR,
            password = addUserRequest.password
        )

        val user = User(
            name = addUserRequest.name,
            email = addUserRequest.email,
            phone = addUserRequest.phone,
            password = hashedPassword
        )

        val result = userWriteOnlyRepository.insertUser(user)
        return if (result) {
            SimpleResponse(isSuccessful = true, message = SuccessCodes.REGISTRATION_COMPLETED.message)
        } else {
            SimpleResponse(isSuccessful = false, message = ErrorCodes.REGISTRATION_ERROR.message)
        }
    }
}