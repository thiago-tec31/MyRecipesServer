package com.br.infra.repository.usersconnections

import com.br.domain.entity.UsersConnections

interface UsersConnectionReadOnlyRepository {
    suspend fun getUsersConnection(userId: String): List<UsersConnections>
    suspend fun existingConnection(userId: String, connectedWithUserId: String): Boolean
}