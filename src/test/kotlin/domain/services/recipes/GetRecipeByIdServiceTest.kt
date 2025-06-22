package domain.services.recipes

import com.br.application.mappers.toRecipeDetailResponse
import com.br.domain.services.recipes.GetRecipeByIdService
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetRecipeByIdServiceTest {

    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private lateinit var getRecipeByIdService: GetRecipeByIdService

    private val recipesAnna = RecipesFactory().create(
        userId = userAnna.id,
        recipeId = UUID.randomUUID().toString(),
        recipesFactoryFake = RecipesFactory.RecipesFactoryFake.Anna
    )

    @BeforeEach
    fun setUp() {
        recipesReadOnlyRepository = mockk()
        getRecipeByIdService = GetRecipeByIdService(recipesReadOnlyRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return user recipes when valid recipeId is provided`() = runBlocking {
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
        val expectedResponse = recipesAnna.toRecipeDetailResponse()

        //THEN
        assertThat(result).isEqualTo(expectedResponse)
    }

}