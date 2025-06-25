package com.br.infra.repository.usersconnections

import com.br.domain.entity.UsersConnections

interface UsersConnectionWriteOnlyRepository {
    suspend fun add(usersConnections: UsersConnections): Boolean
    suspend fun remove(userId: String, connectedWithUserId: String): Boolean
}