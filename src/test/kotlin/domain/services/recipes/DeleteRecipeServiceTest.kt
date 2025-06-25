package domain.services.recipes

import com.br.domain.services.recipes.DeleteRecipeService
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
import domain.model.RecipesFactory
import domain.model.UserFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class DeleteRecipeServiceTest {

    private lateinit var recipesReadOnlyRepository: RecipesReadOnlyRepository
    private lateinit var recipesWriteOnlyRepository: RecipesWriteOnlyRepository

    private lateinit var deleteRecipeService: DeleteRecipeService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private val recipeAnna = RecipesFactory().create(
        userId = userAnna.id,
        recipeId = UUID.randomUUID().toString(),
        recipesFactoryFake = RecipesFactory.RecipesFactoryFake.Anna
    )

    @BeforeEach
    fun setUp() {
        recipesReadOnlyRepository = mockk()
        recipesWriteOnlyRepository = mockk()
        deleteRecipeService = DeleteRecipeService(
            recipesReadOnlyRepository,
            recipesWriteOnlyRepository
        )
    }

    @Test
    fun `should delete recipe successfully`() = runBlocking {
        // GIVEN
        val userId = userAnna.id

        coEvery { recipesReadOnlyRepository.checkIfExists(any()) } returns true
        coEvery { recipesReadOnlyRepository.getById(any(), any()) } returns recipeAnna
        coEvery { recipesWriteOnlyRepository.remove(any()) } returns true

        // WHEN
        val result = deleteRecipeService.delete(recipeId = recipeAnna.id, userId = userId)

        // THEN
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.message).isEqualTo(SuccessCodes.RECIPE_DELETION_SUCCESS.message)
    }

    @Test
    fun `should return error when an error occurs during recipe deletion`() = runBlocking {
        // GIVEN
        val userId = userAnna.id

        coEvery { recipesReadOnlyRepository.checkIfExists(any()) } returns true
        coEvery { recipesReadOnlyRepository.getById(any(), any()) } returns recipeAnna
        coEvery { recipesWriteOnlyRepository.remove(any()) } returns false

        // WHEN
        val result = deleteRecipeService.delete(recipeId = recipeAnna.id, userId = userId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.RECIPE_DELETION_ERROR.message)
    }

    @Test
    fun `should return delete denied when trying to delete a recipe that does not belong to the user`() = runBlocking {
        // GIVEN
        val userId = userAnna.id

        coEvery { recipesReadOnlyRepository.checkIfExists(any()) } returns true
        coEvery { recipesReadOnlyRepository.getById(any(), any()) } returns null

        // WHEN
        val result = deleteRecipeService.delete(recipeId = recipeAnna.id, userId = userId)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.RECIPE_DELETION_DENIED.message)
    }

}