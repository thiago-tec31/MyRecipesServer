package domain.validations

import application.payloads.requests.AuthUserRequestFactory
import com.br.domain.validations.AuthValidationUserRequest
import com.br.domain.validations.AuthValidationUserRequestImpl
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Tag("unit")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthUserRequestValidationTest {

    private lateinit var validator: AuthValidationUserRequest

    private val authUserRequest = AuthUserRequestFactory().create(
        email = "alex@gmail.com", password = "password123"
    )

    @BeforeEach
    fun setUp() {
        validator = AuthValidationUserRequestImpl()
    }

    @Test
    fun `validator should return EMAIL_REQUIRED when email is empty`() = runBlocking {
        val request = authUserRequest.copy(email = "", password = "password123")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.EMAIL_REQUIRED.message)
    }

    @Test
    fun `validator should return INVALID_EMAIL when email is invalid`() = runBlocking {
        val request =  authUserRequest.copy(email = "invalid-email", password = "password123")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.INVALID_EMAIL.message)
    }

    @Test
    fun `validator should return PASSWORD_REQUIRED when password is empty`() = runBlocking {
        val request =  authUserRequest.copy(email = "alex@gmail.com", password = "")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.PASSWORD_REQUIRED.message)
    }

    @Test
    fun `validator should return PASSWORD_TOO_SHORT when password is less than 6 characters`() = runBlocking {
        val request =  authUserRequest.copy(email = "alex@gmail.com", password = "123")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.PASSWORD_TOO_SHORT.message)
    }

    @Test
    fun `validator should return VALID_REGISTRATION when all fields are valid`() = runBlocking {
        val request =  authUserRequest.copy(email = "alex@gmail.com", password = "password123")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.message).isEqualTo(SuccessCodes.VALID_REGISTRATION.message)
    }

}