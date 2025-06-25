package domain.services.usersconnections

import com.br.application.mappers.toUserResponse
import com.br.domain.services.usersconnections.GetUsersConnectionsService
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository
import com.google.common.truth.Truth.assertThat
import domain.model.UserFactory
import domain.model.UsersConnectionsFactory
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
class GetUsersConnectionsServiceTest {

    private lateinit var userReadOnlyRepository: UserReadOnlyRepository
    private lateinit var usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository

    private lateinit var getUsersConnectionsService: GetUsersConnectionsService

    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)
    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private val usersConnections = listOf(
        UsersConnectionsFactory().create(
            usersConnectionFake = UsersConnectionsFactory.UsersConnectionFake.One,
            userId = userAnna.id,
            connectedWithUserId = userAlex.id
        )
    )

    @BeforeEach
    fun setUp() {
        userReadOnlyRepository = mockk()
        usersConnectionReadOnlyRepository = mockk()
        getUsersConnectionsService = GetUsersConnectionsService(
            userReadOnlyRepository,
            usersConnectionReadOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return a list of user connections`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val connectedWithUserId = userAlex.id

        coEvery { usersConnectionReadOnlyRepository.getUsersConnection(userAnnaId) } returns usersConnections
        coEvery { userReadOnlyRepository.findUserById(connectedWithUserId) } returns userAlex

        // WHEN
        val result = getUsersConnectionsService.getConnectionsForUser(userAnnaId)

        // THEN
        val exceptedResponse = listOf(userAlex.toUserResponse())
        assertThat(exceptedResponse).isEqualTo(result)
    }

}