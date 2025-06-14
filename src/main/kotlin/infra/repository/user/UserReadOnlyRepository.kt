package com.br.infra.repository.user

import com.br.domain.entity.User

interface UserReadOnlyRepository {
    suspend fun findUserById(userId: String): User?
    suspend fun findUsersByIds(usersIds: List<String>): List<User>
    suspend fun checkIfUserExists(email: String): Boolean
    suspend fun checkIfUserExistsReturn(email: String): User?
}