package com.br.infra.manager

import com.br.domain.model.Connection
import io.ktor.websocket.WebSocketSession

interface ConnectionManager {
    fun getConnections(userId: String): List<Connection>?
    fun addConnection(userId: String, session: WebSocketSession)
    fun removeConnection(userId: String, session: WebSocketSession?)
    fun hasConnectionsForUser(userId: String): Boolean
}