package com.br.domain.services.users

import com.br.domain.entity.Users
import com.br.infra.repository.users.UserReadOnlyRepository

class GetUserByIdService(
    private val userReadOnlyRepository: UserReadOnlyRepository
) {
    suspend fun getUserById(userId: String): Users? {
        return userReadOnlyRepository.findUserById(userId)
    }
}