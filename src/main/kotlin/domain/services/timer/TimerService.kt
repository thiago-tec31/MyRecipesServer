package com.br.domain.services.timer

import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

class TimerService(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val timers = ConcurrentHashMap<String, Job>()

    fun startTimer(userId: String, durationInSeconds: Int, listener: TimerListener, session: WebSocketSession) {
        timers[userId]?.cancel()

        timers[userId] = scope.launch {
            try {
                for (remainingTime in durationInSeconds downTo 0) {
                    delay(1.seconds)
                    listener.onTimeUpdate(userId, remainingTime)
                }
                listener.onTimeExpired(userId, session)
            } catch (e: Exception) {
                println("Erro ao criar o timer para o usuario: ${e.message}")
            }
        }
    }

    fun resetTimer(userId: String) {
        timers[userId]?.cancel()
        timers.remove(userId)
    }
}