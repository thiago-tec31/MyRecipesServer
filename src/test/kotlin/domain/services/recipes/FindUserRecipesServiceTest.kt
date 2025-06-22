package domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.domain.services.recipes.FindUserRecipesService
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.google.common.truth.Truth.assertThat
import domain.model.RecipesFactory
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
import java.util.UUID

@Tag("unit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FindUserRecipesServiceTest {

    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private lateinit var findUserRecipesService: FindUserRecipesService

    private val recipesAnna = RecipesFactory().create(
        userId = userAnna.id,
        recipeId = UUID.randomUUID().toString(),
        recipesFactoryFake = RecipesFactory.RecipesFactoryFake.Anna
    )

    @BeforeEach
    fun setUp() {
        recipesReadOnlyRepository = mockk()
        findUserRecipesService = FindUserRecipesService(recipesReadOnlyRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return list or user recipes when valid name or ingredient is provided`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val userRecipes = listOf(recipesAnna)

        coEvery { recipesReadOnlyRepository.search(
            nameOrIngredient = eq("lasanha"), userId = eq(userAnnaId))
        } returns userRecipes

        // WHEN
        val result = findUserRecipesService.search("lasanha", userAnnaId)
        val expectedResponse = userRecipes.map { it.toRecipesResponse() }

        // THEN
        assertThat(result).isEqualTo(expectedResponse)
    }

    @Test
    fun `should return empty list when name or ingredient does not match any recipes`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id

        coEvery { recipesReadOnlyRepository.search(
            nameOrIngredient = eq("lasanha"), userId = eq(userAnnaId))
        } returns emptyList()

        // WHEN
        val result = findUserRecipesService.search("lasanha", userAnnaId)

        // THEN
        assertThat(result).isEmpty()
    }
}