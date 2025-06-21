package domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.domain.services.recipes.GetUserRecipesService
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.google.common.truth.Truth.assertThat
import domain.model.RecipesFactory
import domain.model.UserFactory
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetUserRecipesServiceTest {

    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository

    private lateinit var getUserRecipesService: GetUserRecipesService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private val recipesAnna = RecipesFactory().create(
        userId = userAnna.id,
        recipeId = UUID.randomUUID().toString(),
        recipesFactoryFake = RecipesFactory.RecipesFactoryFake.Anna
    )

    @BeforeTest
    fun setUp() {
        recipesReadOnlyRepository = mockk()
        getUserRecipesService = GetUserRecipesService(recipesReadOnlyRepository)
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return list of recipes for user`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val recipesAnna = listOf(recipesAnna)

        coEvery { recipesReadOnlyRepository.getByUser(userAnnaId, null) } returns recipesAnna

        // WHEN
        val result = getUserRecipesService.getAll(userAnnaId,  null)
        val expectedRecipesResponse = recipesAnna.map { it.toRecipesResponse() }

        // THEN
        assertThat(result).isEqualTo(expectedRecipesResponse)
    }

}