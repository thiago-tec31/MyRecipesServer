package domain.services.usersconnections

import com.br.domain.services.usersconnections.AddUsersConnectionsService
import com.br.infra.repository.qrcode.QrCodeWriteOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
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
class AddUsersConnectionsServiceTest {

    private lateinit var qrCodeWriteOnlyRepository: QrCodeWriteOnlyRepository
    private lateinit var usersConnectionWriteOnlyRepository: UsersConnectionWriteOnlyRepository

    private lateinit var addUsersConnectionsService: AddUsersConnectionsService

    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)
    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    @BeforeEach
    fun setUp() {
        qrCodeWriteOnlyRepository = mockk()
        usersConnectionWriteOnlyRepository = mockk()
        addUsersConnectionsService = AddUsersConnectionsService(
            qrCodeWriteOnlyRepository = qrCodeWriteOnlyRepository,
            usersConnectionWriteOnlyRepository = usersConnectionWriteOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return success when user connection is successfully added`() = runBlocking {
        // GIVEN
        val action = true
        val loggedInUserId = userAlex.id
        val userToConnectedId = userAnna.id

        coEvery { qrCodeWriteOnlyRepository.remove(any()) } returns true
        coEvery { usersConnectionWriteOnlyRepository.add(any()) } returns true

        // WHEN
        val result = addUsersConnectionsService.add(action, loggedInUserId, userToConnectedId)

        // THEN
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.message).isEqualTo(SuccessCodes.CONNECTION_SUCCESS.message)
    }

    @Test
    fun `should return failure when loggedInUserId is null`() = runBlocking {
        // GIVEN
        val action = true
        val userToConnectedId = userAnna.id

        // WHEN
        val result = addUsersConnectionsService.add(action, null, userToConnectedId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.INVALID_PARAMETERS.message)
    }

    @Test
    fun `should return failure when userToConnectId is null`() = runBlocking {
        // GIVEN
        val action = true
        val loggedInUserId = userAlex.id

        // WHEN
        val result = addUsersConnectionsService.add(action, loggedInUserId, null)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.INVALID_PARAMETERS.message)
    }

    @Test
    fun `should return failure when qrCode deletion fails`() = runBlocking {
        // GIVEN
        val action = true
        val loggedInUserId = userAlex.id
        val userToConnectedId = userAnna.id

        coEvery { qrCodeWriteOnlyRepository.remove(any()) } returns false

        // WHEN
        val result = addUsersConnectionsService.add(action, loggedInUserId, userToConnectedId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.DELETION_QRCODE_ERROR.message)
    }

    @Test
    fun `should return failure when adding user connection fails`() = runBlocking {
        // GIVEN
        val action = true
        val loggedInUserId = userAlex.id
        val userToConnectedId = userAnna.id

        coEvery { qrCodeWriteOnlyRepository.remove(any()) } returns true
        coEvery { usersConnectionWriteOnlyRepository.add(any()) } returns false

        // WHEN
        val result = addUsersConnectionsService.add(action, loggedInUserId, userToConnectedId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.CONNECTION_ERROR.message)
    }

    @Test
    fun `should return failure when action is false`() = runBlocking {
        // GIVEN
        val action = false
        val loggedInUserId = userAlex.id
        val userToConnectedId = userAnna.id

        // WHEN
        val result = addUsersConnectionsService.add(action, loggedInUserId, userToConnectedId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.CONNECTION_REFUSED.message)
    }

}