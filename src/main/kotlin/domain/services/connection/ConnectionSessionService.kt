package com.br.domain.services.connection

import com.br.application.payloads.responses.AwaitingConnectinResponse
import com.br.application.payloads.responses.CancelConnectionResponse
import com.br.application.payloads.responses.ConnectionStatusResponse
import com.br.application.payloads.responses.ErrorMessageResponse
import com.br.application.payloads.responses.QrCodeExpiredResponse
import com.br.application.payloads.responses.TimeRemainingResponse
import com.br.application.payloads.responses.UsersConnectionsResponse
import com.br.domain.services.timer.TimerListener
import com.br.domain.services.timer.TimerService
import com.br.infra.manager.ConnectionManager
import com.br.util.Constants
import com.br.util.GsonUtil
import com.br.util.SuccessCodes
import io.ktor.websocket.CloseReason
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.send

class ConnectionSessionService(
    private val timerService: TimerService,
    private val connectionManager: ConnectionManager
): TimerListener {

    private val connectionClosedReason = CloseReason(
        CloseReason.Codes.NORMAL, SuccessCodes.CONNECTION_CLOSED_SUCCESS.message
    )
    private val qrCodeExpiredReason = CloseReason(
        CloseReason.Codes.NORMAL, SuccessCodes.EXPIRED_QR_CODE.message
    )

    fun startConnectionSessionAndTimer(userId: String, session: WebSocketSession) {
        connectionManager.addConnection(userId, session)
        timerService.startTimer(userId, Constants.DURATION_IN_SECONDS_QRCODE, this)
    }

    fun startConnectionSession(userId: String, session: WebSocketSession) {
        connectionManager.addConnection(userId, session)
    }

    fun resetTimer(userId: String) {
        timerService.resetTimer(userId)
    }

    override suspend fun onTimeUpdate(userId: String, remainingTime: Int) {
        val timeRemainingResponse = TimeRemainingResponse(
            duration = remainingTime
        )
        sendMessageToUser(userId, timeRemainingResponse)
    }

    override suspend fun onTimeExpired(userId: String) {
        sendQrCodeExpiredToUser(userId)
        timerService.resetTimer(userId)
    }

    suspend fun sendMessageUsersConnection(
        userId: String,
        usersConnectionsResponse: UsersConnectionsResponse?
    ) {
        usersConnectionsResponse?.let {
            sendMessageToUser(userId, it)
            resetTimer(userId)
        }
    }

    suspend fun sendMessageAwaitingConnectingToUser(userId: String, message: String) {
        val awaitingConnectinResponse = AwaitingConnectinResponse(message = message)
        sendMessageToUser(userId, awaitingConnectinResponse)
    }

    suspend fun sendConnectionSuccessfulForUsers(
        userId: String,
        userToConnectId: String,
        message: String
    ) {
        notifyUserOfConnectionStatus(userId, userToConnectId, true, message)
    }

    suspend fun sendConnectionRefusedForUsers(
        userId: String,
        userToConnectId: String,
        message: String
    ) {
        notifyUserOfConnectionStatus(userId, userToConnectId, false, message)
    }

    suspend fun removeAndCloseSession(userId: String, session: WebSocketSession) {
        closeUserSessions(userId, connectionClosedReason)
        connectionManager.removeConnection(userId, session)
    }

    suspend fun removeAndCloseMultipleSession(
        userId: String,
        userToConnectId: String,
        session: WebSocketSession
    ) {
        executeActionForUserConnections(listOf(userId, userToConnectId)) {
            it.close(connectionClosedReason)
        }
        connectionManager.removeConnection(userId, session)
        connectionManager.removeConnection(userToConnectId, session)
    }

    suspend fun sendErrorMessage(userId: String, message: String) {
        val errorMessageResponse = ErrorMessageResponse(message = message)
        sendMessageToUser(userId, errorMessageResponse)
    }

    suspend fun sendCancelConnectionMessage(userId: String, message: String) {
        val cancelConnectionResponse = CancelConnectionResponse(message = message)
        sendMessageToUser(userId, cancelConnectionResponse)
    }

    private suspend fun executeActionForUserConnections(
        userIds: List<String>,
        action: suspend (WebSocketSession) -> Unit,
    ) {
        userIds.forEach { userId ->
            connectionManager.getConnections(userId)?.forEach { connection ->
                action(connection.session)
            }
        }
    }

    private suspend fun notifyUserOfConnectionStatus(
        userId: String,
        userToConnectId: String,
        isAccepted: Boolean,
        message: String
    ) {
        val connectionStatusResponse = ConnectionStatusResponse(
            connectionAccepted = isAccepted,
            message = message
        )
        sendMessageToUser(userId, connectionStatusResponse)
        sendMessageToUser(userToConnectId, connectionStatusResponse)
    }

    private suspend fun sendMessageToUser(userId: String, message: Any) {
        val jsonMessage = GsonUtil.serialize(message)
        val connections = connectionManager.getConnections(userId)

        return if (!connections.isNullOrEmpty()) {
            connections.forEach { connection ->
                connection.session.send(jsonMessage)
            }
        } else {
            println("Nenhuma conexão encontrada para o usuario: $userId")
        }
    }


    private suspend fun sendQrCodeExpiredToUser(userId: String) {
        val qrCodeExpiredResponse = QrCodeExpiredResponse(
            message = SuccessCodes.EXPIRED_QR_CODE.message
        )
        sendMessageToUser(userId, qrCodeExpiredResponse)
        removeAndCloseSessionByQrCodeExpired(userId, null)
    }

    private suspend fun removeAndCloseSessionByQrCodeExpired(userId: String, session: WebSocketSession?) {
        closeUserSessions(userId, qrCodeExpiredReason)
        connectionManager.removeConnection(userId, session)
    }

    private suspend fun closeUserSessions(userId: String, closeReason: CloseReason) {
        connectionManager.getConnections(userId)?.forEach { connection ->
            connection.session.close(closeReason)
        }
    }
}