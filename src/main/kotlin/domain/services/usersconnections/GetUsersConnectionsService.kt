package com.br.domain.services.usersconnections

import com.br.application.mappers.toUserResponse
import com.br.application.payloads.responses.UserResponse
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository

class GetUsersConnectionsService(
    private val userReadOnlyRepository: UserReadOnlyRepository,
    private val usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository
) {
    suspend fun getConnectionsForUser(userId: String): List<UserResponse> {
        val connectionsEntities = usersConnectionReadOnlyRepository.getUsersConnection(userId)
        return connectionsEntities.mapNotNull { usersConnections ->
            userReadOnlyRepository.findUserById(usersConnections.connectedWithUserId)?.toUserResponse()
        }
    }
}