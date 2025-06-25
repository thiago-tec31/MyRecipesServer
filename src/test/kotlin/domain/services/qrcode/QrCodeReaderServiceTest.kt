package domain.services.qrcode

import com.br.domain.services.qrcode.QrCodeReaderService
import com.br.infra.repository.qrcode.QrCodeReadOnlyRepository
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
import domain.model.QrCodeFactory
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
class QrCodeReaderServiceTest {

    private lateinit var qrCodeReadOnlyRepository: QrCodeReadOnlyRepository
    private lateinit var userReadOnlyRepository: UserReadOnlyRepository
    private lateinit var usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository

    private lateinit var qrCodeReaderService: QrCodeReaderService

    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)
    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private val qrCodeAlex = QrCodeFactory().create(QrCodeFactory.QrCodeFake.QrCode, userAlex.id)

    @BeforeEach
    fun setUp() {
        qrCodeReadOnlyRepository = mockk()
        userReadOnlyRepository = mockk()
        usersConnectionReadOnlyRepository = mockk()
        qrCodeReaderService = QrCodeReaderService(
            qrCodeReadOnlyRepository = qrCodeReadOnlyRepository,
            userReadOnlyRepository =  userReadOnlyRepository,
            usersConnectionReadOnlyRepository = usersConnectionReadOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return ReadQrCodeResponse with successful true`() = runBlocking {
        // GIVEN
        val code = qrCodeAlex.id
        val userAnnaId = userAnna.id

        coEvery { userReadOnlyRepository.findUserById(userAnnaId) } returns userAnna
        coEvery { qrCodeReadOnlyRepository.getQrCode(code) } returns qrCodeAlex
        coEvery { usersConnectionReadOnlyRepository.existingConnection(any(), any()) } returns false

        // WHEN
        val result = qrCodeReaderService.readQrCode(code, userAnnaId)

        // THEN
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.message).isEqualTo(SuccessCodes.QR_CODE_SUCCESS.message)
    }

    @Test
    fun `should return ReadQrCodeResponse with CONNECTION_DENIED message`() = runBlocking {
        // GIVEN
        val code = qrCodeAlex.id
        val userAlexId = userAlex.id

        coEvery { userReadOnlyRepository.findUserById(userAlexId) } returns userAlex
        coEvery { qrCodeReadOnlyRepository.getQrCode(code) } returns qrCodeAlex
        coEvery { usersConnectionReadOnlyRepository.existingConnection(any(), any()) } returns false

        // WHEN
        val result = qrCodeReaderService.readQrCode(code, userAlexId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.CONNECTION_DENIED.message)
    }

    @Test
    fun `should return ReadQrCodeResponse with EXISTING_CONNECTION message`() = runBlocking {
        // GIVEN
        val code = qrCodeAlex.id
        val userAnnaId = userAnna.id

        coEvery { userReadOnlyRepository.findUserById(userAnnaId) } returns userAnna
        coEvery { qrCodeReadOnlyRepository.getQrCode(code) } returns qrCodeAlex
        coEvery { usersConnectionReadOnlyRepository.existingConnection(any(), any()) } returns true

        // WHEN
        val result = qrCodeReaderService.readQrCode(code, userAnnaId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.EXISTING_CONNECTION.message)
    }

    @Test
    fun `should return ReadQrCodeResponse with CODE_NOT_FOUND message`() = runBlocking {
        // GIVEN
        val code = "1234"
        val userAnnaId = userAnna.id

        coEvery { userReadOnlyRepository.findUserById(userAnnaId) } returns userAnna
        coEvery { qrCodeReadOnlyRepository.getQrCode(code) } returns null

        // WHEN
        val result = qrCodeReaderService.readQrCode(code, userAnnaId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.CODE_NOT_FOUND.message)
    }

}