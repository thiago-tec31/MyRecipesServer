package domain.services.recipes

import com.br.application.mappers.toRecipeDetailResponse
import com.br.domain.services.recipes.GetRecipeByIdService
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
class GetRecipeByIdServiceTest {

    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository
    private lateinit var usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)
    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)

    private lateinit var getRecipeByIdService: GetRecipeByIdService

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
        getRecipeByIdService = GetRecipeByIdService(
            recipesReadOnlyRepository,
            usersConnectionReadOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return recipe that belongs to the user`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val recipeId = recipesAnna.id

        coEvery {
            recipesReadOnlyRepository.getById(
                recipeId = eq(recipeId),
                userId = eq(userAnnaId)
            )
        } returns recipesAnna

        // WHEN
        val result = getRecipeByIdService.getRecipeById(recipeId, userAnnaId)

        //THEN
        val expectedResponse = recipesAnna.toRecipeDetailResponse()
        assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `should return a recipe that was shared with another user`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val connectedWithUserId = usersConnections.first().connectedWithUserId

        coEvery { recipesReadOnlyRepository.getById(any(), any()) } returns null
        coEvery { usersConnectionReadOnlyRepository.getUsersConnection(userId = eq(userAnnaId), ) } returns usersConnections
        coEvery { recipesReadOnlyRepository.getById(recipeId = eq(recipesAlex.id), eq(connectedWithUserId)) } returns recipesAlex

        // WHEN
        val result = getRecipeByIdService.getRecipeById(recipesAlex.id, userAnnaId)

        //THEN
        val expectedResponse = recipesAlex.toRecipeDetailResponse()
        assertThat(result).isEqualTo(expectedResponse)
    }

}