package domain.services.usersconnections

import com.br.domain.services.usersconnections.RemoveUsersConnectionsService
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository
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
class RemoveUsersConnectionsServiceTest {

    private lateinit var usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository
    private lateinit var usersConnectionWriteOnlyRepository: UsersConnectionWriteOnlyRepository

    private lateinit var removeUsersConnectionsService: RemoveUsersConnectionsService

    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)
    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    @BeforeEach
    fun setUp() {
        usersConnectionReadOnlyRepository = mockk()
        usersConnectionWriteOnlyRepository = mockk()
        removeUsersConnectionsService = RemoveUsersConnectionsService(
            usersConnectionReadOnlyRepository,
            usersConnectionWriteOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should remove connection successfully when valid users are provided`() = runBlocking {
        // GIVEN
        val userAlexId = userAlex.id
        val userToDisconnected = userAnna.id

        coEvery { usersConnectionReadOnlyRepository.existingConnection(any(), any()) } returns true
        coEvery { usersConnectionWriteOnlyRepository.remove(any(), any()) } returns true

        // WHEN
        val result = removeUsersConnectionsService.removeConnectionsForUser(userAlexId, userToDisconnected)

        // THEN
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.message).isEqualTo(SuccessCodes.CONNECTION_REMOVED_SUCCESS.message)
    }

    @Test
    fun `should return failure when user to disconnect does not exist`() = runBlocking {
        // GIVEN
        val userAlexId = userAlex.id
        val userToDisconnected = userAnna.id

        coEvery { usersConnectionReadOnlyRepository.existingConnection(any(), any()) } returns false

        // WHEN
        val result = removeUsersConnectionsService.removeConnectionsForUser(userAlexId, userToDisconnected)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.USER_NOT_EXIST_TO_DISCONNECT.message)
    }

    @Test
    fun `should return failure when removing connection fails`() = runBlocking {
        // GIVEN
        val userAlexId = userAlex.id
        val userToDisconnected = userAnna.id

        coEvery { usersConnectionReadOnlyRepository.existingConnection(any(), any()) } returns true
        coEvery { usersConnectionWriteOnlyRepository.remove(any(), any()) } returns false

        // WHEN
        val result = removeUsersConnectionsService.removeConnectionsForUser(userAlexId, userToDisconnected)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.CONNECTION_REMOVE_ERROR.message)
    }
}