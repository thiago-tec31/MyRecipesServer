package domain.model

import com.br.domain.entity.QrCode
import java.util.UUID

class QrCodeFactory {

    fun create(qrCodeFake: QrCodeFake, userId: String) = when(qrCodeFake) {
        QrCodeFake.QrCode -> {
            QrCode(
                code = UUID.randomUUID().toString(),
                userId = userId
            )
        }
    }

    sealed interface QrCodeFake {
        data object QrCode : QrCodeFake
    }
}