package domain.services.users

import domain.model.UserFactory
import com.br.domain.services.users.GetUserByIdService
import com.br.infra.repository.users.UserReadOnlyRepository
import com.google.common.truth.Truth.assertThat
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
class GetUserByIdServiceTest {

    private lateinit var userReadOnlyRepository: UserReadOnlyRepository

    private lateinit var getUserByIdService: GetUserByIdService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    @BeforeEach
    fun setUp() {
        userReadOnlyRepository = mockk()
        getUserByIdService = GetUserByIdService(userReadOnlyRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return user when valid ID is provide`() = runBlocking {
        // GIVEN
        val userId = userAnna.id
        coEvery { userReadOnlyRepository.findUserById(eq(userId)) } returns userAnna

        // WHEN
        val result = getUserByIdService.getUserById(userId)

        // THEN
        assertThat(result).isEqualTo(userAnna)
    }

    @Test
    fun `should return null when no exist user ID is provide`() = runBlocking {
        // GIVEN
        val userId = userAnna.id
        coEvery { userReadOnlyRepository.findUserById(eq(userId)) } returns null

        // WHEN
        val result = getUserByIdService.getUserById(userId)

        // THEN
        assertThat(result).isNull()
    }
}