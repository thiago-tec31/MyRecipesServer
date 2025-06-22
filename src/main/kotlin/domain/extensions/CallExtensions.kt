package com.br.domain.extensions

import com.br.domain.entity.Users
import com.br.domain.exceptions.UserAuthenticationNotFoundException
import com.br.util.ErrorCodes
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication

fun ApplicationCall.getUserAuthentication() : String {
    val usersModel = authentication.principal<Users>()
    if (usersModel != null) {
        return usersModel.id
    } else {
        throw UserAuthenticationNotFoundException(ErrorCodes.USER_NOT_LOGGED_IN.message)
    }
}