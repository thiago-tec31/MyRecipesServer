package com.br.infra.repository.user

import com.br.domain.entity.Users

interface UserReadOnlyRepository {
    suspend fun findUserById(userId: String): Users?
    suspend fun findUsersByIds(usersIds: List<String>): List<Users>
    suspend fun checkIfUserExists(email: String): Boolean
    suspend fun checkIfUserExistsReturn(email: String): Users?
}