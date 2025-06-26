package com.br.domain.services.qrcode

import com.br.application.payloads.responses.ReadQrCodeResponse
import com.br.application.payloads.responses.SimpleResponse
import com.br.application.payloads.responses.UsersConnectionsResponse
import com.br.domain.entity.QrCode
import com.br.domain.entity.Users
import com.br.infra.repository.qrcode.QrCodeReadOnlyRepository
import com.br.infra.repository.users.UserReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class QrCodeReaderService(
    private val qrCodeReadOnlyRepository: QrCodeReadOnlyRepository,
    private val userReadOnlyRepository: UserReadOnlyRepository,
    private val usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository
) {
    suspend fun readQrCode(code: String, userId: String): ReadQrCodeResponse {
        val qrCode = qrCodeReadOnlyRepository.getQrCode(code)
            ?: return createErrorResponse(message = ErrorCodes.CODE_NOT_FOUND.message, qrCodeUserId = null)

        val userModel = userReadOnlyRepository.findUserById(userId)
            ?: return createErrorResponse(message = ErrorCodes.USER_NOT_FOUND.message, qrCodeUserId = qrCode.userId)

        val simpleResponse = validateQrCodeAndConnection(qrCode, userModel)

        if (!simpleResponse.isSuccessful) {
            return createErrorResponse(message = simpleResponse.message, qrCodeUserId = qrCode.userId)
        }

        val usersConnectionsResponse = UsersConnectionsResponse(
            name = userModel.name,
            userToConnectId = userId
        )

        return ReadQrCodeResponse(
            isSuccessful = true,
            message = SuccessCodes.QR_CODE_SUCCESS.message,
            qrCodeCreatorId = qrCode.userId,
            usersConnectionsResponse = usersConnectionsResponse
        )
    }

    private suspend fun validateQrCodeAndConnection(
        qrCode: QrCode,
        userModel: Users
    ) : SimpleResponse {
        if (qrCode.userId == userModel.id) {
            return SimpleResponse(isSuccessful = false, message = ErrorCodes.CONNECTION_DENIED.message)
        }

        val isExistingConnection = usersConnectionReadOnlyRepository.existingConnection(
            userId = userModel.id,
            connectedWithUserId = qrCode.userId
        )

        if (isExistingConnection) {
            return SimpleResponse(isSuccessful = false, message = ErrorCodes.EXISTING_CONNECTION.message)
        }

        return SimpleResponse(isSuccessful = true, message = SuccessCodes.VALIDATION_SUCCESS.message)
    }

    private fun createErrorResponse(message: String, qrCodeUserId: String?): ReadQrCodeResponse {
        return ReadQrCodeResponse(
            isSuccessful = false,
            message = message,
            qrCodeCreatorId = qrCodeUserId.orEmpty()
        )
    }
}