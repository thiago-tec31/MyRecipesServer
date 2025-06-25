package com.br.infra.repository.qrcode

import com.br.domain.services.database.DatabaseService
import com.br.domain.entity.QrCode
import com.br.util.Constants
import com.br.util.ErrorCodes
import com.mongodb.MongoException
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
        try {
            val filter = Filters.eq(QrCode::code.name, code)
            return qrCodeCollection.find(filter).firstOrNull()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return null
    }

    override suspend fun add(qrCode: QrCode): Boolean {
        try {
            val filter = Filters.eq(QrCode::userId.name, qrCode.userId)
            val result = qrCodeCollection.find(filter).firstOrNull()
            return if (result != null) {
                qrCodeCollection.updateOne(
                    filter = Filters.eq(QrCode::userId.name, qrCode.userId),
                    update = Updates.combine(
                        Updates.set(QrCode::code.name, qrCode.code)
                    )
                ).wasAcknowledged()
            } else {
                qrCodeCollection.insertOne(qrCode).wasAcknowledged()
            }
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }

    override suspend fun remove(userId: String): Boolean {
        try {
            val result = qrCodeCollection.deleteOne(Filters.eq(QrCode::userId.name, userId))
            return result.wasAcknowledged()
        } catch (e: Exception) {
            when(e) {
                is MongoException -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
                else -> logger.error("${ErrorCodes.DATABASE_ERROR}: ${e.message}", e)
            }
        }
        return false
    }
}