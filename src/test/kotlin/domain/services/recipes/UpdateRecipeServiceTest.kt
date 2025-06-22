package domain.services.recipes

import application.payloads.requests.AddUpdateRecipesRequestFactory
import application.payloads.response.SimpleResponseFactory
import com.br.domain.services.recipes.UpdateRecipeService
import com.br.domain.validations.AddValidationRecipeRequest
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
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
class UpdateRecipeServiceTest {

    private lateinit var addValidationRecipeRequest: AddValidationRecipeRequest
    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository
    private lateinit var recipesWriteOnlyRepository: RecipesWriteOnlyRepository

    private lateinit var updateRecipeService: UpdateRecipeService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private val recipeAnna = RecipesFactory().create(
        userId = userAnna.id,
        recipeId = UUID.randomUUID().toString(),
        recipesFactoryFake = RecipesFactory.RecipesFactoryFake.Anna
    )

    private val addUpdateRecipesRequest = AddUpdateRecipesRequestFactory().create()
    private val simpleResponse = SimpleResponseFactory().create(
        isSuccessfully = true,
        message = SuccessCodes.VALID_REGISTRATION.message
    )

    @BeforeEach
    fun setUp() {
        addValidationRecipeRequest = mockk()
        recipesReadOnlyRepository = mockk()
        recipesWriteOnlyRepository = mockk()

        updateRecipeService = UpdateRecipeService(
            recipesReadOnlyRepository,
            recipesWriteOnlyRepository,
            addValidationRecipeRequest
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should must update the recipe successfully`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val recipeId = recipeAnna.id

        coEvery { recipesReadOnlyRepository.checkIfExists(any()) } returns true
        coEvery { addValidationRecipeRequest.validator(any(), any()) } returns simpleResponse

        coEvery { recipesWriteOnlyRepository.update(any(), any()) } returns true

        // WHEN
        val result = updateRecipeService.update(userAnnaId, recipeId, addUpdateRecipesRequest)

        // THEN
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.message).isEqualTo(SuccessCodes.RECIPE_UPDATE_SUCCESS.message)
    }

    @Test
    fun `should return a recipe update error if fails`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id
        val recipeId = recipeAnna.id

        coEvery { recipesReadOnlyRepository.checkIfExists(any()) } returns true
        coEvery { addValidationRecipeRequest.validator(any(), any()) } returns simpleResponse

        coEvery { recipesWriteOnlyRepository.update(any(), any()) } returns false

        // WHEN
        val result = updateRecipeService.update(userAnnaId, recipeId, addUpdateRecipesRequest)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.RECIPE_UPDATE_ERROR.message)
    }

}