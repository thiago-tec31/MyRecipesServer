package domain.validations

import application.payloads.requests.RegisterUserRequestFactory
import com.br.domain.validations.AddValidationUserRequest
import com.br.domain.validations.AddValidationUserRequestImpl
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddValidationUserRequestTest {

    private lateinit var validator: AddValidationUserRequest

    private val registerUserRequest = RegisterUserRequestFactory().create()

    @BeforeEach
    fun setUp() {
        validator = AddValidationUserRequestImpl()
    }

    @Test
    fun `validator should return NAME_REQUIRED when name is empty`() = runBlocking {
        val request = registerUserRequest.copy(
            name = "",
            email = "alex@gmail.com",
            password = "password123",
            phone = "12 9 1234-5678"
        )
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.NAME_REQUIRED.message)
    }

    @Test
    fun `validator should return EMAIL_REQUIRED when email is empty`() = runBlocking {
        val request =
            registerUserRequest.copy(name = "Alex", email = "", password = "password123", phone = "12 9 1234-5678")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.EMAIL_REQUIRED.message)
    }

    @Test
    fun `validator should return INVALID_EMAIL when email is invalid`() = runBlocking {
        val request = registerUserRequest.copy(
            name = "Alex",
            email = "invalid-email",
            password = "password123",
            phone = "12 9 1234-5678"
        )
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.INVALID_EMAIL.message)
    }

    @Test
    fun `validator should return PASSWORD_REQUIRED when password is empty`() = runBlocking {
        val request =
            registerUserRequest.copy(name = "Alex", email = "alex@gmail.com", password = "", phone = "12 9 1234-5678")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.PASSWORD_REQUIRED.message)
    }

    @Test
    fun `validator should return PASSWORD_TOO_SHORT when password is less than 6 characters`() = runBlocking {
        val request = registerUserRequest.copy(
            name = "Alex",
            email = "alex@gmail.com",
            password = "123",
            phone = "12 9 1234-5678"
        )
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.PASSWORD_TOO_SHORT.message)
    }

    @Test
    fun `validator should return PHONE_REQUIRED when phone is empty`() = runBlocking {
        val request =
            registerUserRequest.copy(name = "Alex", email = "alex@gmail.com", password = "password123", phone = "")
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.PHONE_REQUIRED.message)
    }

    @Test
    fun `validator should return INVALID_PHONE_FORMAT when phone is invalid`() = runBlocking {
        val request = registerUserRequest.copy(
            name = "Alex",
            email = "alex@gmail.com",
            password = "password123",
            phone = "invalid-phone"
        )
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.message).isEqualTo(ErrorCodes.INVALID_PHONE_FORMAT.message)
    }

    @Test
    fun `validator should return VALID_REGISTRATION when all fields are valid`() = runBlocking {
        val request = registerUserRequest.copy(
            name = "Alex",
            email = "alex@gmail.com",
            password = "password123",
            phone = "12 9 1234-5678"
        )
        val response = validator.validator(request)

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.message).isEqualTo(SuccessCodes.VALID_REGISTRATION.message)
    }

}