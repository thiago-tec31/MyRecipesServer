package com.br.domain.extensions

import com.br.domain.entity.User
import com.br.domain.exceptions.UserAuthenticationNotFoundException
import com.br.util.ErrorCodes
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication

fun ApplicationCall.getUserAuthentication() : String {
    val userModel = authentication.principal<User>()
    if (userModel != null) {
        return userModel.id
    } else {
        throw UserAuthenticationNotFoundException(ErrorCodes.USER_NOT_LOGGED_IN.message)
    }
}