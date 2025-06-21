package domain.services.users

import application.payloads.requests.RegisterUserRequestFactory
import application.payloads.response.SimpleResponseFactory
import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.services.users.RegisterUserService
import com.br.domain.validations.AddValidationUserRequest
import fake_util.Constants
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.infra.repository.user.UserWriteOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AddUserServiceTest {

    private lateinit var addValidationUserRequest: AddValidationUserRequest
    private lateinit var bCryptPasswordService: BCryptPasswordService
    private lateinit var userReadOnlyRepository: UserReadOnlyRepository
    private lateinit var userWriteOnlyRepository: UserWriteOnlyRepository

    private lateinit var registerUserService: RegisterUserService

    private val registerUserRequest = RegisterUserRequestFactory().create()

    private val simpleResponse = SimpleResponseFactory().create(
        isSuccessfully = true,
        message = SuccessCodes.VALID_REGISTRATION.message
    )

    @BeforeTest
    fun setUp() {
        addValidationUserRequest = mockk()
        bCryptPasswordService = mockk()
        userReadOnlyRepository = mockk()
        userWriteOnlyRepository = mockk()

        registerUserService = RegisterUserService(
            addValidationUserRequest, bCryptPasswordService, userWriteOnlyRepository, userReadOnlyRepository
        )
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should successfully register user when all conditions are met`() = runBlocking {
        // GIVEN
        val hashedPassword = Constants.FAKE_PASSWORD_HASHED

        coEvery { addValidationUserRequest.validator(any()) } returns simpleResponse
        coEvery { userReadOnlyRepository.checkIfUserExists(any()) } returns false
        coEvery { bCryptPasswordService.hashedPassword(any(), any()) } returns hashedPassword

        coEvery { userWriteOnlyRepository.insertUser(any()) } returns true

        // WHEN
        val result = registerUserService.register(registerUserRequest)

        // THEN
        assertThat(result.isSuccessful).isTrue()
        assertThat(result.message).isEqualTo(SuccessCodes.REGISTRATION_COMPLETED.message)
    }

    @Test
    fun `should return error when email is already in use`() = runBlocking {
        // GIVEN
        coEvery { addValidationUserRequest.validator(any()) } returns simpleResponse
        coEvery { userReadOnlyRepository.checkIfUserExists(any()) } returns true

        // WHEN
        val result = registerUserService.register(registerUserRequest)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.EMAIL_ALREADY_USED.message)
    }

    @Test
    fun `should return error when user insertion fails`() = runBlocking {
        // GIVEN
        val hashedPassword = Constants.FAKE_PASSWORD_HASHED

        coEvery { addValidationUserRequest.validator(any()) } returns simpleResponse
        coEvery { userReadOnlyRepository.checkIfUserExists(any()) } returns false
        coEvery { bCryptPasswordService.hashedPassword(any(), any()) } returns hashedPassword

        coEvery { userWriteOnlyRepository.insertUser(any()) } returns false

        // WHEN
        val result = registerUserService.register(registerUserRequest)

        // THEN
        assertThat(result.isSuccessful).isFalse()
        assertThat(result.message).isEqualTo(ErrorCodes.REGISTRATION_ERROR.message)
    }
}