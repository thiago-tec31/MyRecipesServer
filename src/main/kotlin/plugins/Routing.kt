package com.br.plugins

import com.br.application.routes.usersRoute
import com.br.domain.services.user.RegisterUserService
import com.br.domain.services.user.GetProfileUserService
import com.br.domain.services.user.LoginUserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val registerUserService by inject<RegisterUserService>()
    val loginUserService by inject<LoginUserService>()
    val getProfileUserService by inject<GetProfileUserService>()

    install(Routing) {
        usersRoute(registerUserService, loginUserService, getProfileUserService)
    }
}
