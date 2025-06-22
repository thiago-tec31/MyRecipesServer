package domain.services.users

import application.payloads.requests.AuthUserRequestFactory
import application.payloads.response.TokenResponseFactory
import domain.model.UserFactory
import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.services.token.TokenService
import com.br.domain.services.users.LoginUserService
import com.br.domain.validations.AuthValidationUserRequest
import fake_util.Constants
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginUserServiceTest {

    private lateinit var tokenService: TokenService
    private lateinit var bCryptPasswordService: BCryptPasswordService
    private lateinit var authUserRequestValidation: AuthValidationUserRequest
    private lateinit var userReadOnlyRepository: UserReadOnlyRepository


    private lateinit var loginUserService: LoginUserService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    private val authUserRequestFactory = AuthUserRequestFactory().create(
        userAnna.email,
        userAnna.password
    )

    private val tokenResponseFactory = TokenResponseFactory().create(
        isSuccessful = true,
        message = SuccessCodes.VALID_REGISTRATION.message,
        token = Constants.FAKE_TOKEN
    )

    @BeforeEach
    fun setUp() {
        tokenService = mockk()
        bCryptPasswordService = mockk()
        authUserRequestValidation = mockk()
        userReadOnlyRepository = mockk()

        loginUserService = LoginUserService(
            tokenService, authUserRequestValidation, bCryptPasswordService, userReadOnlyRepository
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return a successful tokenResponse with the generated token`() = runBlocking {
        // GIVEN
        val token = Constants.FAKE_TOKEN

        coEvery { authUserRequestValidation.validator(any()) } returns tokenResponseFactory
        coEvery { userReadOnlyRepository.checkIfUserExistsReturn(any()) } returns userAnna

        every { bCryptPasswordService.verifyPassword(any(), any()) } returns true
        every { tokenService.generateToken(any()) } returns token

        // WHEN
        val result = loginUserService.loginUser(authUserRequestFactory)

        // THEN
        Truth.assertThat(result.token).isNotEmpty()
        Truth.assertThat(result.isSuccessful).isTrue()
    }

    @Test
    fun `should return user email not found message response`() = runBlocking {
        // GIVEN

        coEvery { authUserRequestValidation.validator(any()) } returns tokenResponseFactory
        coEvery { userReadOnlyRepository.checkIfUserExistsReturn(any()) } returns null

        // WHEN
        val result = loginUserService.loginUser(authUserRequestFactory)

        // THEN
        Truth.assertThat(result.token).isNull()
        Truth.assertThat(result.isSuccessful).isFalse()
        Truth.assertThat(result.message).isEqualTo(ErrorCodes.USER_EMAIL_NOT_FOUND.message)
    }

    @Test
    fun `should return incorrect password response`() = runBlocking {
        // GIVEN

        coEvery { authUserRequestValidation.validator(any()) } returns tokenResponseFactory
        coEvery { userReadOnlyRepository.checkIfUserExistsReturn(any()) } returns userAnna
        every { bCryptPasswordService.verifyPassword(any(), any()) } returns false

        // WHEN
        val result = loginUserService.loginUser(authUserRequestFactory)

        // THEN
        Truth.assertThat(result.token).isNull()
        Truth.assertThat(result.isSuccessful).isFalse()
        Truth.assertThat(result.message).isEqualTo(ErrorCodes.INCORRECT_PASSWORD.message)
    }
}