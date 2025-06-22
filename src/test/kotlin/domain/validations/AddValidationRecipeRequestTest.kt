package domain.validations

import application.payloads.requests.AddUpdateRecipesRequestFactory
import com.br.domain.validations.AddValidationRecipeRequest
import com.br.domain.validations.AddValidationRecipeRequestImpl
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
import domain.model.UserFactory
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddValidationRecipeRequestTest {

    private lateinit var addValidationRecipeRequest: AddValidationRecipeRequest

    private val request = AddUpdateRecipesRequestFactory().create()
    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    @BeforeEach
    fun setUp() {
        addValidationRecipeRequest = AddValidationRecipeRequestImpl()
    }

    @Test
    fun `should validate recipe successfully when input is valid`() = runBlocking {

        // GIVEN
        val userAnnaId = userAnna.id

        // WHEN
        val response = addValidationRecipeRequest.validator(request, userAnnaId)

        // THEN
        assertThat(response.isSuccessful).isTrue()
        assertThat(response.message).isEqualTo(SuccessCodes.VALID_REGISTRATION.message)
    }

    @Test
    fun `should return error when user ID is null`() = runBlocking {

        // WHEN
        val response = addValidationRecipeRequest.validator(request, null)

        // THEN
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.USER_ID_NOT_FOUND.message)
    }

    @Test
    fun `should return error when recipe title is empty`() = runBlocking {

        // GIVEN
        val userAnnaId = userAnna.id
        val recipesRequest = request.copy(name = "")

        // WHEN
        val response = addValidationRecipeRequest.validator(recipesRequest, userAnnaId)

        // THEN
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.TITLE_REQUIRED.message)
    }

    @Test
    fun `should return error when recipe category is negative`() = runBlocking {

        // GIVEN
        val userAnnaId = userAnna.id
        val recipesRequest = request.copy(category = -1)

        // WHEN
        val response = addValidationRecipeRequest.validator(recipesRequest, userAnnaId)

        // THEN
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.CATEGORY_REQUIRED.message)
    }

    @Test
    fun `should return error when preparation time is empty`() = runBlocking {

        // GIVEN
        val userAnnaId = userAnna.id
        val recipesRequest = request.copy(preparationTime = "")

        // WHEN
        val response = addValidationRecipeRequest.validator(recipesRequest, userAnnaId)

        // THEN
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.PREPARATION_TIME_REQUIRED.message)
    }

    @Test
    fun `should return error when preparation mode is empty`() = runBlocking {

        // GIVEN
        val userAnnaId = userAnna.id
        val recipesRequest = request.copy(preparationMode = "")

        // WHEN
        val response = addValidationRecipeRequest.validator(recipesRequest, userAnnaId)

        // THEN
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.PREPARATION_MODE_REQUIRED.message)
    }

    @Test
    fun `should return error when ingredients list is empty`() = runBlocking {

        // GIVEN
        val userAnnaId = userAnna.id
        val recipesRequest = request.copy(ingredients = emptyList())

        // WHEN
        val response = addValidationRecipeRequest.validator(recipesRequest, userAnnaId)

        // THEN
        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.INGREDIENTS_REQUIRED.message)
    }

}