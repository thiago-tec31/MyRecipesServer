package com.br.domain.model

import io.ktor.websocket.WebSocketSession

data class Connection(
    val userId: String,
    val session: WebSocketSession
)
