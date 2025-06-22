package com.br.application.routes

import com.br.application.payloads.requests.RegisterUserRequest
import com.br.application.payloads.requests.AuthUserRequest
import com.br.domain.extensions.getUserAuthentication
import com.br.domain.services.users.RegisterUserService
import com.br.domain.services.users.GetProfileUserService
import com.br.domain.services.users.LoginUserService
import com.br.util.Constants
import com.br.util.ErrorCodes
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.usersRoute(
    addUserService: RegisterUserService,
    loginUserService: LoginUserService,
    getProfileUserService: GetProfileUserService
) {
    route(Constants.USER_ROUTE) {
        authenticate {
            getProfile(getProfileUserService)
        }
        register(addUserService)
        login(loginUserService)
    }
}

fun Route.getProfile(getProfileUserService: GetProfileUserService) {
    get("/profile") {
        try {
            val userId = call.getUserAuthentication()
            val userResponse = getProfileUserService.getProfileUserById(userId)
            if (userResponse != null) {
                call.respond(HttpStatusCode.OK, userResponse)
            } else {
                call.respond(HttpStatusCode.BadRequest, ErrorCodes.UNKNOWN_ERROR.message)
            }
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.register(
    addUserService: RegisterUserService
) {
    post("/register") {
        try {
            val request = call.receiveNullable<RegisterUserRequest>()
            if (request != null) {
                val simpleResponse = addUserService.register(request)
                if (simpleResponse.isSuccessful) {
                    call.respond(HttpStatusCode.Created, simpleResponse)
                } else {
                    call.respond(HttpStatusCode.BadRequest, simpleResponse)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, ErrorCodes.UNKNOWN_ERROR)
            }
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.login(
    loginUserService: LoginUserService
) {
    post("/login") {
        try {
            val request = call.receiveNullable<AuthUserRequest>()
            if (request != null) {
                val tokenResponse = loginUserService.loginUser(request)
                if (tokenResponse.isSuccessful) {
                    call.respond(HttpStatusCode.OK, tokenResponse)
                } else {
                    call.respond(HttpStatusCode.BadRequest, tokenResponse)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, ErrorCodes.UNKNOWN_ERROR)
            }
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}