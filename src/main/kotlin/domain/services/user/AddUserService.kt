package com.br.domain.services.user

import application.payloads.request.AddUserRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.domain.entity.User
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.infra.repository.user.UserWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCode

class AddUserService(
    private val userWriteOnlyRepository: UserWriteOnlyRepository,
    private val userReadOnlyRepository: UserReadOnlyRepository
) {
    suspend fun addUser(addUserRequest: AddUserRequest): SimpleResponse {
        val user = User(
            name = addUserRequest.name,
            email = addUserRequest.email,
            phone = addUserRequest.phone,
            password = addUserRequest.password
        )

        val result = userWriteOnlyRepository.insertUser(user)
        return if (result) {
            SimpleResponse(successful = true, message = SuccessCode.REGISTRATION_COMPLETED.message)
        } else {
            SimpleResponse(successful = false, message = ErrorCodes.REGISTRATION_ERROR.message)
        }
    }
}