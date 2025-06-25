package com.br.domain.services.timer

interface TimerListener {
    suspend fun onTimeUpdate(userId: String, remainingTime: Int)
    suspend fun onTimeExpired(userId: String)
}