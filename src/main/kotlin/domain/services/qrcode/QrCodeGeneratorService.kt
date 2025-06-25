package com.br.domain.services.qrcode

import com.br.application.payloads.responses.GenerateQrCodeResponse
import com.br.domain.entity.QrCode
import com.br.infra.repository.qrcode.QrCodeWriteOnlyRepository
import org.bson.types.ObjectId
import qrcode.QRCode
import qrcode.color.Colors
import java.util.Base64

class QrCodeGeneratorService(
    private val qrCodeWriteOnlyRepository: QrCodeWriteOnlyRepository
) {
    suspend fun generateQrCode(userId: String): GenerateQrCodeResponse? {
        val qrCode = QrCode(
            code = ObjectId().toHexString(),
            userId = userId
        )

        val result = qrCodeWriteOnlyRepository.add(qrCode)
        if (!result) {
            return null
        }

        val qrCodeImage = generateQrCodeImage(qrCode.code)
            ?: return null

        val qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeImage)

        return GenerateQrCodeResponse(
            qrCodeBase64 = qrCodeBase64,
            userId = userId
        )
    }

    private fun generateQrCodeImage(code: String): ByteArray? {
        return try {
            val qrCode = QRCode.ofSquares()
                .withSize(8)
                .withColor(Colors.BLACK)
                .build(code)

            qrCode.renderToBytes()
        } catch (e: Exception) {
            null
        }
    }
}