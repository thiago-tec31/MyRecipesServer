package com.br.domain.services.user

import com.br.domain.entity.User
import com.br.infra.repository.user.UserReadOnlyRepository

class GetUserByIdService(
    private val userReadOnlyRepository: UserReadOnlyRepository
) {
    suspend fun getUserById(userId: String): User? {
        return userReadOnlyRepository.findUserById(userId)
    }
}