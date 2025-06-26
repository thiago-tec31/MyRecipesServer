package com.br.infra.repository.qrcode

import com.br.domain.services.database.DatabaseService
import com.br.domain.entity.QrCode
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory

class QrCodeRepository(
    databaseService: DatabaseService
) : QrCodeReadOnlyRepository, QrCodeWriteOnlyRepository {

    private val logger = LoggerFactory.getLogger(QrCodeRepository::class.java)
    private val qrCodeCollection = databaseService.database.getCollection<QrCode>(Constants.COLLECTION_NAME_QR_CODES)

    override suspend fun getQrCode(code: String): QrCode? {
        return try {
            val filter = Filters.eq(QrCode::code.name, code)
            qrCodeCollection.find(filter).firstOrNull()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            null
        }
    }

    override suspend fun add(qrCode: QrCode): Boolean {
        return try {
            val filter = Filters.eq(QrCode::userId.name, qrCode.userId)
            val result = qrCodeCollection.find(filter).firstOrNull()
            if (result != null) {
                qrCodeCollection.updateOne(
                    filter = Filters.eq(QrCode::userId.name, qrCode.userId),
                    update = Updates.set(QrCode::code.name, qrCode.code)
                ).wasAcknowledged()
            } else {
                qrCodeCollection.insertOne(qrCode).wasAcknowledged()
            }
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }

    override suspend fun remove(userId: String): Boolean {
        return try {
            val result = qrCodeCollection.deleteOne(Filters.eq(QrCode::userId.name, userId))
            result.wasAcknowledged()
        } catch (ex: Exception) {
            logger.error("${ErrorCodes.DATABASE_ERROR.message}: ${ex.message}", ex)
            false
        }
    }
}