package domain.services.qrcode

import com.br.domain.services.qrcode.QrCodeGeneratorService
import com.br.infra.repository.qrcode.QrCodeWriteOnlyRepository
import com.google.common.truth.Truth.assertThat
import domain.model.UserFactory
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Tag("unit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QrCodeGeneratorServiceTest {

    private lateinit var qrCodeWriteOnlyRepository: QrCodeWriteOnlyRepository

    private lateinit var qrCodeGeneratorService: QrCodeGeneratorService

    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)

    @BeforeEach
    fun setUp() {
        qrCodeWriteOnlyRepository = mockk()
        qrCodeGeneratorService = QrCodeGeneratorService(
            qrCodeWriteOnlyRepository = qrCodeWriteOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return generateQrCodeResponse with qrcode and userId`() = runBlocking {
        // GIVEN
        val userId = userAlex.id

        coEvery { qrCodeWriteOnlyRepository.add(any()) } returns true

        // WHEN
        val result = qrCodeGeneratorService.generateQrCode(userId)

        // THEN
        assertThat(result).isNotNull()
        assertThat(result?.qrCodeBase64).isNotNull()
    }

}