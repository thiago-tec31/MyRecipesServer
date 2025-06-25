package domain.services.recipes

import application.payloads.requests.AddUpdateRecipesRequestFactory
import application.payloads.response.SimpleResponseFactory
import com.br.domain.services.recipes.CreateRecipeService
import com.br.domain.validations.AddValidationRecipeRequest
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
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

@Tag("unit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateRecipeServiceTest {

    private lateinit var addValidationRecipeRequest: AddValidationRecipeRequest
    private lateinit var recipesWriteOnlyRepository: RecipesWriteOnlyRepository

    private lateinit var createRecipeService: CreateRecipeService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)
    private val addUpdateRecipesRequest = AddUpdateRecipesRequestFactory().create()

    private val simpleResponse = SimpleResponseFactory().create(
        isSuccessfully = true,
        message = SuccessCodes.VALID_REGISTRATION.message
    )

    @BeforeEach
    fun setUp() {
        addValidationRecipeRequest = mockk()
        recipesWriteOnlyRepository = mockk()

        createRecipeService = CreateRecipeService(
            addValidationRecipeRequest,
            recipesWriteOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return success when adding a valid recipe`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id

        coEvery { addValidationRecipeRequest.validator(any(), any()) } returns simpleResponse
        coEvery { recipesWriteOnlyRepository.create(any()) } returns true

        // WHEN
        val result = createRecipeService.createRecipe(addUpdateRecipesRequest, userAnnaId)

        // THEN
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.message).isEqualTo(SuccessCodes.RECIPE_REGISTRATION_SUCCESS.message)
    }

    @Test
    fun `should return success failure recipes addition fails`() = runBlocking {
        // GIVEN
        val userAnnaId = userAnna.id

        coEvery { addValidationRecipeRequest.validator(any(), any()) } returns simpleResponse
        coEvery { recipesWriteOnlyRepository.create(any()) } returns false

        // WHEN
        val result = createRecipeService.createRecipe(addUpdateRecipesRequest, userAnnaId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.RECIPE_REGISTRATION_ERROR.message)
    }


}