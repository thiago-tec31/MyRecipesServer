package com.br.infra.repository.qrcode

import com.br.domain.entity.QrCode

interface QrCodeReadOnlyRepository {
    suspend fun getQrCode(code: String): QrCode?
}