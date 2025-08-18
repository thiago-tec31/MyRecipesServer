package com.br.domain.services.timer

import io.ktor.websocket.WebSocketSession

interface TimerListener {
    suspend fun onTimeUpdate(userId: String, remainingTime: Int)
    suspend fun onTimeExpired(userId: String, session: WebSocketSession)
}