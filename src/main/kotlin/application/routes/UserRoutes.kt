package com.br.application.routes

import application.payloads.request.AddUserRequest
import com.br.application.payloads.request.AuthUserRequest
import com.br.domain.entity.User
import com.br.domain.extensions.getUserAuthentication
import com.br.domain.services.user.AddUserService
import com.br.domain.services.user.GetProfileUserService
import com.br.domain.services.user.LoginUserService
import com.br.util.Constants
import com.br.util.ErrorCodes
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.usersRoute(
    addUserService: AddUserService,
    loginUserService: LoginUserService,
    getProfileUserService: GetProfileUserService
) {
    route(Constants.USER_ROUTE) {
        authenticate {
            getUserProfile(getProfileUserService)
        }
        createUser(addUserService)
        loginUser(loginUserService)
    }
}

fun Route.getUserProfile(getProfileUserService: GetProfileUserService) {
    get("/profile") {
        try {
            val userId = call.getUserAuthentication()
            val userResponse = getProfileUserService.getProfileUserById(userId)
            call.respond(userResponse)
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.createUser(
    addUserService: AddUserService
) {
    post("/register") {
        try {
            val request = call.receiveNullable<AddUserRequest>()
            if (request != null) {
                val simpleResponse = addUserService.addUser(request)
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

fun Route.loginUser(
    loginUserService: LoginUserService
) {
    post("/login") {
        try {
            val request = call.receiveNullable<AuthUserRequest>()
            if (request != null) {
                val simpleResponse = loginUserService.loginUser(request)
                if (simpleResponse.isSuccessful) {
                    call.respond(HttpStatusCode.OK, simpleResponse)
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