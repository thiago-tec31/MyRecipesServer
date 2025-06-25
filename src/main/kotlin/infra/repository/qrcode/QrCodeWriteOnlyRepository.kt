package com.br.infra.repository.qrcode

import com.br.domain.entity.QrCode

interface QrCodeWriteOnlyRepository {
    suspend fun add(qrCode: QrCode): Boolean
    suspend fun remove(userId: String): Boolean
}