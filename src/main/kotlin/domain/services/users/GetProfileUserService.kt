package com.br.domain.services.users

import com.br.application.mappers.toUserResponse
import com.br.application.payloads.responses.UserResponse
import com.br.infra.repository.user.UserReadOnlyRepository

class GetProfileUserService(
    private val userReadOnlyRepository: UserReadOnlyRepository
) {
    suspend fun getProfileUserById(userId: String): UserResponse? {
        val userModel = userReadOnlyRepository.findUserById(userId)
        return userModel?.toUserResponse()
    }
}