package com.br.application.routes

import application.payloads.request.AddUserRequest
import com.br.domain.services.user.AddUserService
import com.br.util.Constants
import com.br.util.ErrorCodes
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.usersRoute(
    addUserService: AddUserService
) {
    route(Constants.USER_ROUTE) {
        createUser(addUserService)
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