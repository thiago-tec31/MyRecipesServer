package com.br.domain.services.usersconnections

import com.br.application.payloads.responses.SimpleResponse
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class RemoveUsersConnectionsService(
    private val usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository,
    private val usersConnectionWriteOnlyRepository: UsersConnectionWriteOnlyRepository
) {
    suspend fun removeConnectionsForUser(userId: String, connectedWithUserId: String) : SimpleResponse {
        return when {
            !doesConnectionExists(userId, connectedWithUserId) -> {
                SimpleResponse(isSuccessful = false, message = ErrorCodes.USER_NOT_EXIST_TO_DISCONNECT.message)
            }

            isConnectedRemoved(userId, connectedWithUserId) -> {
                SimpleResponse(isSuccessful = true, message = SuccessCodes.CONNECTION_REMOVED_SUCCESS.message)
            }

            else -> {
                SimpleResponse(isSuccessful = false, message = ErrorCodes.CONNECTION_REMOVE_ERROR.message)
            }
        }
    }

    private suspend fun doesConnectionExists(userId: String,  connectedWithUserId: String): Boolean {
        return usersConnectionReadOnlyRepository.existingConnection(
            userId = userId,
            connectedWithUserId = connectedWithUserId
        )
    }

    private suspend fun isConnectedRemoved(userId: String,  connectedWithUserId: String) : Boolean {
        return usersConnectionWriteOnlyRepository.remove(userId, connectedWithUserId)
    }
}