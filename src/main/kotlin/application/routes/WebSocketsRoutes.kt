package com.br.application.routes

import com.br.application.payloads.requests.SendQrCodeRequest
import com.br.application.payloads.requests.UserActionRequest
import com.br.application.payloads.responses.GenerateQrCodeResponse
import com.br.domain.extensions.getUserAuthentication
import com.br.domain.services.connection.ConnectionSessionService
import com.br.domain.services.qrcode.QrCodeGeneratorService
import com.br.domain.services.qrcode.QrCodeReaderService
import com.br.domain.services.usersconnections.AddUsersConnectionsService
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.br.util.GsonUtil
import com.br.util.SuccessCodes
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.isActive

fun Route.webSocketsRoutes(
    qrCodeReaderService: QrCodeReaderService,
    connectionSessionService: ConnectionSessionService,
    qrCodeGeneratorService: QrCodeGeneratorService,
    addUsersConnectionsService: AddUsersConnectionsService
) {
    route(Constants.WEBSOCKETS_ROUTE) {
        authenticate {
            createQrCodeSession(
                qrCodeGeneratorService = qrCodeGeneratorService,
                addUsersConnectionsService = addUsersConnectionsService,
                connectionSessionService = connectionSessionService
            )
            readQrCodeSession(
                qrCodeReaderService = qrCodeReaderService,
                connectionSessionService = connectionSessionService
            )
        }
    }
}

fun Route.createQrCodeSession(
    connectionSessionService: ConnectionSessionService,
    qrCodeGeneratorService: QrCodeGeneratorService,
    addUsersConnectionsService: AddUsersConnectionsService
) {
    webSocket(Constants.CREATE_QR_CODE_ROUTE) {
        val userId = call.getUserAuthentication()
        val generateQrCode: GenerateQrCodeResponse? = try {
            qrCodeGeneratorService.generateQrCode(userId)
        } catch (ex: Exception) {
            println("Erro ao gerar o QR Code: ${ex.message}")
            send(ErrorCodes.QR_CODE_GENERATION_FAILED.message)
            close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, ErrorCodes.QR_CODE_GENERATION_FAILED.message))
            return@webSocket
        }

        if (generateQrCode == null) {
            send(ErrorCodes.QR_CODE_GENERATION_FAILED.message)
            close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, ErrorCodes.QR_CODE_GENERATION_FAILED.message))
            return@webSocket
        }

        try {
            connectionSessionService.startConnectionSessionAndTimer(
                session = this,
                userId = generateQrCode.userId
            )
            send(GsonUtil.serialize(GenerateQrCodeResponse::class.java, generateQrCode))

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    try {
                        val userActionRequest = GsonUtil.deserialize(UserActionRequest::class.java, receivedText)

                        if (userActionRequest.cancelOperation) {
                            connectionSessionService.sendCancelConnectionMessage(
                                userId = userId,
                                message = Constants.OPERATION_CANCELED_BY_USER_MESSAGE
                            )
                            connectionSessionService.resetTimer(userId)
                            connectionSessionService.removeAndCloseSession(userId, this)
                            break
                        }

                        val simpleResponse = addUsersConnectionsService.add(
                            isConnectionAccepted = userActionRequest.action,
                            loggedInUserId = generateQrCode.userId,
                            userToConnected = userActionRequest.userToConnectionId
                        )

                        when {
                            simpleResponse.isSuccessful -> {
                                connectionSessionService.sendConnectionSuccessfulForUsers(
                                    userId = generateQrCode.userId,
                                    userToConnectId = userActionRequest.userToConnectionId.orEmpty(),
                                    message = simpleResponse.message
                                )
                                connectionSessionService.removeAndCloseMultipleSession(
                                    userId = generateQrCode.userId,
                                    userToConnectId = userActionRequest.userToConnectionId.orEmpty(),
                                    session = this
                                )
                            }

                            simpleResponse.message == ErrorCodes.INVALID_PARAMETERS.message -> {
                                connectionSessionService.sendErrorMessage(
                                    userId = generateQrCode.userId,
                                    message = simpleResponse.message
                                )
                            }

                            else -> {
                                connectionSessionService.sendConnectionRefusedForUsers(
                                    userId = generateQrCode.userId,
                                    userToConnectId = userActionRequest.userToConnectionId.orEmpty(),
                                    message = simpleResponse.message
                                )
                                connectionSessionService.removeAndCloseMultipleSession(
                                    userId = generateQrCode.userId,
                                    userToConnectId = userActionRequest.userToConnectionId.orEmpty(),
                                    session = this
                                )
                                connectionSessionService.onTimeExpired(userId = generateQrCode.userId, session = this)
                            }
                        }
                    } catch (ex: Exception) {
                        println("Erro ao processar a mensagem: ${ex.message}")
                        ex.printStackTrace()
                    }
                }
            }
        } catch (ex: ClosedReceiveChannelException) {
            println("Conexão fechada: ${closeReason.await()}")
        } catch (ex: Throwable) {
            println("Erro no WebSocket: ${closeReason.await()}")
            ex.printStackTrace()
        } finally {
            if (this.isActive) {
                connectionSessionService.removeAndCloseSession(generateQrCode.userId, this)
            }
        }
    }
}

fun Route.readQrCodeSession(
    qrCodeReaderService: QrCodeReaderService,
    connectionSessionService: ConnectionSessionService
) {
    webSocket(Constants.READ_QR_CODE_ROUTE) {
        val userId = call.getUserAuthentication()
        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val receivedText = frame.readText()
                    connectionSessionService.startConnectionSession(userId, this)
                    val sendQrCodeRequest = try {
                        GsonUtil.deserialize(SendQrCodeRequest::class.java, receivedText)
                    } catch (ex: Exception) {
                        println("Erro ao deserializar mensagem: ${ex.message}")
                        connectionSessionService.sendErrorMessage(
                            userId = userId,
                            message = ErrorCodes.INVALID_PARAMETERS.message
                        )
                        connectionSessionService.removeAndCloseSession(userId, this)
                        continue
                    }

                    val readQrCodeResponse = qrCodeReaderService.readQrCode(sendQrCodeRequest.qrcode, userId)

                    when {
                        readQrCodeResponse.isSuccessful -> {
                            connectionSessionService.resetTimer(readQrCodeResponse.qrCodeCreatorId)
                            connectionSessionService.sendMessageUsersConnection(
                                userId = readQrCodeResponse.qrCodeCreatorId,
                                usersConnectionsResponse = readQrCodeResponse.usersConnectionsResponse
                            )
                            connectionSessionService.sendMessageAwaitingConnectingToUser(
                                userId = userId,
                                message = SuccessCodes.WAIT_FOR_USER_TO_ACCEPT_CONNECTION.message
                            )
                        }
                        else -> {
                            connectionSessionService.sendErrorMessage(
                                userId = userId,
                                message = readQrCodeResponse.message
                            )
                            connectionSessionService.removeAndCloseSession(userId, this)
                        }
                    }
                }
            }
        } catch (ex: ClosedReceiveChannelException) {
            println("Conexão fechada: ${closeReason.await()}")
            ex.printStackTrace()
        } catch (ex: Throwable) {
            println("Erro no WebSocket: ${closeReason.await()}")
            ex.printStackTrace()
        } finally {
            if (this.isActive) {
                connectionSessionService.removeAndCloseSession(userId, this)
            }
        }
    }
}

