package com.br.domain.services.qrcode

import com.br.application.payloads.responses.GenerateQrCodeResponse
import com.br.domain.entity.QrCode
import com.br.domain.exceptions.QrCodeServiceException
import com.br.infra.repository.qrcode.QrCodeWriteOnlyRepository
import org.bson.types.ObjectId
import qrcode.QRCode
import qrcode.color.Colors
import java.util.Base64

class QrCodeGeneratorService(
    private val qrCodeWriteOnlyRepository: QrCodeWriteOnlyRepository
) {
    suspend fun generateQrCode(userId: String): GenerateQrCodeResponse {

        if (userId.isBlank()) {
            throw IllegalArgumentException("userId não pode ser vazio.")
        }

        val qrCode = QrCode(
            code = ObjectId().toHexString(),
            userId = userId
        )

        val result = qrCodeWriteOnlyRepository.add(qrCode)
        if (!result) {
            throw QrCodeServiceException("Falha ao adicionar o Qr Code ao repositorio.")
        }

        val qrCodeImage = generateQrCodeImage(qrCode.code)
            ?: throw QrCodeServiceException("Erro ao gerar a imagem do Qr Code.")

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
        } catch (ex: Exception) {
            throw QrCodeServiceException("Falha ao gerar a imagem do Qr Code ${ex.message}")
        }
    }
}