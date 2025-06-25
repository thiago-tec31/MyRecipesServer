package com.br.domain.services.usersconnections

import com.br.application.payloads.responses.SimpleResponse
import com.br.domain.entity.UsersConnections
import com.br.infra.repository.qrcode.QrCodeWriteOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class AddUsersConnectionsService(
    private val qrCodeWriteOnlyRepository: QrCodeWriteOnlyRepository,
    private val usersConnectionWriteOnlyRepository: UsersConnectionWriteOnlyRepository
) {

    suspend fun add(
        isConnectionAccepted: Boolean,
        loggedInUserId: String?,
        userToConnected: String?
    ) : SimpleResponse {
        if (!areUserIdsValid(loggedInUserId, userToConnected)) {
            return SimpleResponse(isSuccessful = false, message = ErrorCodes.INVALID_PARAMETERS.message)
        }

        if (isConnectionAccepted) {
            if (!isQRCodeDeleted(loggedInUserId.orEmpty())) {
                return SimpleResponse(isSuccessful = false, message = ErrorCodes.DELETION_QRCODE_ERROR.message)
            }

            return if (saveConnections(loggedInUserId.orEmpty(), userToConnected.orEmpty())) {
                SimpleResponse(isSuccessful = true, message = SuccessCodes.CONNECTION_SUCCESS.message)
            } else {
                SimpleResponse(isSuccessful = true, message = ErrorCodes.CONNECTION_ERROR.message)
            }

        } else {
            return SimpleResponse(isSuccessful = false, message = ErrorCodes.CONNECTION_REFUSED.message)
        }
    }

    private suspend fun saveConnections(loggedInUserId: String, userToConnected: String): Boolean {
        val firstConnection = UsersConnections(userId = loggedInUserId, connectedWithUserId = userToConnected)
        val secondConnection = UsersConnections(userId = userToConnected, connectedWithUserId = loggedInUserId)

        return usersConnectionWriteOnlyRepository.add(firstConnection) &&
                usersConnectionWriteOnlyRepository.add(secondConnection)
    }

    private fun areUserIdsValid(loggedInUserId: String?, userToConnected: String?): Boolean {
        return !loggedInUserId.isNullOrBlank() && !userToConnected.isNullOrBlank()
    }

    private suspend fun isQRCodeDeleted(userId: String): Boolean {
        return qrCodeWriteOnlyRepository.remove(userId)
    }
}