package com.br.plugins

import com.br.application.routes.usersRoute
import com.br.domain.services.user.AddUserService
import com.br.domain.services.user.LoginUserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val addUserService by inject<AddUserService>()
    val loginUserService by inject<LoginUserService>()

    install(Routing) {
        usersRoute(addUserService, loginUserService)
    }
}
