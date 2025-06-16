package com.br.domain.validations

import com.br.application.payloads.requests.AuthUserRequestFactory
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class AuthUserRequestValidationTest {

    private lateinit var validator: AuthUserRequestValidation

    private val authUserRequest = AuthUserRequestFactory().create(
        email = "alex@gmail.com", password = "password123"
    )

    @BeforeTest
    fun setUp() {
        validator = AuthUserRequestValidationImpl()
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