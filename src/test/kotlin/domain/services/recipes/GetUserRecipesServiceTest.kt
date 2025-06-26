package domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.domain.entity.CategoryEnum
import com.br.domain.services.recipes.GetUserRecipesService
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.users.UserReadOnlyRepository
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
class GetUserRecipesServiceTest {

    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository
    private lateinit var usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository
    private lateinit var userReadOnlyRepository: UserReadOnlyRepository

    private lateinit var getUserRecipesService: GetUserRecipesService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)
    private val userAlex = UserFactory().create(UserFactory.UserFake.Alex)

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
        userReadOnlyRepository = mockk()
        getUserRecipesService = GetUserRecipesService(
            recipesReadOnlyRepository,
            userReadOnlyRepository,
            usersConnectionReadOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return list of user recipes or shared if available`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val userAlexId = userAlex.id
        val category = 1

        val recipesAnna = listOf(recipesAnna)
        val recipesAlex = listOf(recipesAlex)

        coEvery { recipesReadOnlyRepository.getByUser(
            userId = eq(userAnnaId),
            categoryEnum = CategoryEnum.fromInt(category)
        ) } returns recipesAnna

        coEvery {
            recipesReadOnlyRepository.getByUser(
                userId = eq(userAlexId),
                categoryEnum = CategoryEnum.fromInt(category)
            )
        } returns recipesAlex

        coEvery { usersConnectionReadOnlyRepository.getUsersConnection(eq(userAnnaId)) } returns usersConnections
        coEvery { userReadOnlyRepository.findUsersByIds(eq(listOf(userAlexId))) } returns listOf(userAlex)

        // WHEN
        val result = getUserRecipesService.getAll(userAnnaId, category)

        // THEN
        val expectedResponse = (recipesAnna + recipesAlex).map { recipe ->
            val ownerName = if (usersConnections.isNotEmpty()) {
                if (recipe.userId == userAlex.id) userAlex.name else null
            } else null
            recipe.toRecipesResponse().copy(ownerName = ownerName)
        }

        assertThat(expectedResponse).isEqualTo(result)
    }

}