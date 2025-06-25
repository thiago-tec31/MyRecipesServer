package com.br.infra.manager

import com.br.domain.model.Connection
import io.ktor.websocket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

class ConnectionManagerImpl: ConnectionManager {

    private val connections = ConcurrentHashMap<String, MutableList<Connection>>()

    override fun getConnections(userId: String): List<Connection> {
        return connections[userId] ?: emptyList()
    }

    override fun addConnection(userId: String, session: WebSocketSession) {
        val thisConnection = Connection(userId, session)
        connections.computeIfAbsent(userId) { mutableListOf() }.add(thisConnection)
    }

    override fun removeConnection(userId: String, session: WebSocketSession?) {
        connections[userId]?.removeIf { it.session == session }

        if (connections[userId].isNullOrEmpty()) {
            connections.remove(userId)
        }
    }

    override fun hasConnectionsForUser(userId: String): Boolean {
        return connections.containsKey(userId) &&
                connections[userId]?.isNotEmpty() == true
    }

}