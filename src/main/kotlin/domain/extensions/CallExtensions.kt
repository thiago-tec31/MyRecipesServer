package com.br.domain.extensions

import com.br.domain.entity.Users
import com.br.domain.exceptions.UserAuthenticationNotFoundException
import com.br.util.ErrorCodes
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal

fun ApplicationCall.getUserAuthentication() : String {
    val user = principal<Users>()
    return user?.id ?: throw UserAuthenticationNotFoundException(ErrorCodes.USER_NOT_LOGGED_IN.message)
}