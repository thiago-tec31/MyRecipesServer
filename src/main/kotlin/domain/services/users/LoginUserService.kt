package com.br.domain.services.users

import com.br.application.payloads.requests.AuthUserRequest
import com.br.application.payloads.responses.TokenResponse
import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.services.token.TokenService
import com.br.domain.validations.AuthValidationUserRequest
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.util.ErrorCodes
import com.br.util.SuccessCodes

class LoginUserService(
    private val tokenService: TokenService,
    private val authValidationUserRequest: AuthValidationUserRequest,
    private val bCryptPasswordService: BCryptPasswordService,
    private val userReadOnlyRepository: UserReadOnlyRepository
) {
    suspend fun loginUser(request: AuthUserRequest): TokenResponse {
        val tokenResponse = authValidationUserRequest.validator(request)
        if (!tokenResponse.isSuccessful) {
            return tokenResponse
        }

        val userModel = userReadOnlyRepository.checkIfUserExistsReturn(request.email)
            ?: return TokenResponse(isSuccessful = false, message = ErrorCodes.USER_EMAIL_NOT_FOUND.message)

        val hashedPassword = userModel.password
        val verifyPassword = bCryptPasswordService.verifyPassword(
            password = request.password.toCharArray(),
            hashedPassword = hashedPassword
        )

        if (verifyPassword) {
            val token = tokenService.generateToken(userId = userModel.id)
            return TokenResponse(isSuccessful = true, message = SuccessCodes.LOGIN_SUCCESS.message, token = token)
        } else {
            return TokenResponse(isSuccessful = false, message = ErrorCodes.INCORRECT_PASSWORD.message)
        }

    }
}