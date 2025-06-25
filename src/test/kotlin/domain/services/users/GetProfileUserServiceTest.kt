package domain.services.users

import com.br.application.mappers.toUserResponse
import domain.model.UserFactory
import com.br.domain.services.users.GetProfileUserService
import com.br.infra.repository.user.UserReadOnlyRepository
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
class GetProfileUserServiceTest {

    private lateinit var userReadOnlyRepository: UserReadOnlyRepository

    private lateinit var getProfileUserService: GetProfileUserService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    @BeforeEach
    fun setUp() {
        userReadOnlyRepository = mockk()
        getProfileUserService = GetProfileUserService(userReadOnlyRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return user profile when valid ID is provided`() = runBlocking {
        // GIVEN
        val userId = userAnna.id
        coEvery { userReadOnlyRepository.findUserById(eq(userId)) } returns userAnna

        // WHEN
        val result = getProfileUserService.getProfileUserById(userId)
        val expectedResponse = userAnna.toUserResponse()

        // THEN
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `should return null when user ID is not found`() = runBlocking {
        // GIVEN
        val userId = "1234"
        coEvery { userReadOnlyRepository.findUserById(eq(userId)) } returns null

        // WHEN
        val result = getProfileUserService.getProfileUserById(userId)

        // THEN
        assertThat(result).isNull()
    }
}