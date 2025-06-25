package domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.domain.services.recipes.FindUserRecipesService
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository
import com.google.common.truth.Truth.assertThat
import domain.model.RecipesFactory
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
import java.util.UUID

@Tag("unit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindUserRecipesServiceTest {

    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository
    private lateinit var usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)
    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)

    private lateinit var findUserRecipesService: FindUserRecipesService

    private val usersConnectionsFactory = UsersConnectionsFactory().create(
        usersConnectionFake = UsersConnectionsFactory.UsersConnectionFake.One,
        userId = userAnna.id,
        connectedWithUserId = userAlex.id
    )

    private val usersConnections = listOf(usersConnectionsFactory)

    private val recipesAnna = RecipesFactory().create(
        userId = userAnna.id,
        recipeId = UUID.randomUUID().toString(),
        recipesFactoryFake = RecipesFactory.RecipesFactoryFake.Anna
    )

    private val recipesAlex = RecipesFactory().create(
        userId = userAlex.id,
        recipeId = UUID.randomUUID().toString(),
        recipesFactoryFake = RecipesFactory.RecipesFactoryFake.Alex
    )

    @BeforeEach
    fun setUp() {
        recipesReadOnlyRepository = mockk()
        usersConnectionReadOnlyRepository = mockk()
        findUserRecipesService = FindUserRecipesService(
            recipesReadOnlyRepository,
            usersConnectionReadOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return user recipes including shared ones from connected users`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val connectedWithUserId = userAlex.id

        val recipesAnna = listOf(recipesAnna)
        val recipesAlex = listOf(recipesAlex)

        coEvery { recipesReadOnlyRepository.search(
            nameOrIngredient = eq("lasanha"), userId = eq(userAnnaId))
        } returns recipesAnna

        coEvery { recipesReadOnlyRepository.search(
            nameOrIngredient = eq("lasanha"), userId = eq(connectedWithUserId))
        } returns recipesAlex

        coEvery { usersConnectionReadOnlyRepository.getUsersConnection(userId = eq(userAnnaId), ) } returns usersConnections

        // WHEN
        val result = findUserRecipesService.search("lasanha", userAnnaId)

        // THEN
        val expectedResponse = (recipesAnna + recipesAlex).map { it.toRecipesResponse() }
        assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `should return empty list when name or ingredient does not match any recipes`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val connectedWithUserId = userAlex.id

        coEvery { recipesReadOnlyRepository.search(
            nameOrIngredient = eq("qualquer coisa"), userId = eq(userAnnaId))
        } returns emptyList()

        coEvery { recipesReadOnlyRepository.search(
            nameOrIngredient = eq("qualquer coisa"), userId = eq(connectedWithUserId))
        } returns emptyList()

        coEvery { usersConnectionReadOnlyRepository.getUsersConnection(userId = eq(userAnnaId), ) } returns emptyList()

        // WHEN
        val result = findUserRecipesService.search("qualquer coisa", userAnnaId)

        // THEN
        assertThat(result).isEmpty()
    }
}