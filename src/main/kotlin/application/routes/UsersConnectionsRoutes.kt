package com.br.application.routes

import com.br.domain.extensions.getUserAuthentication
import com.br.domain.services.usersconnections.GetUsersConnectionsService
import com.br.domain.services.usersconnections.RemoveUsersConnectionsService
import com.br.util.Constants
import com.br.util.Constants.PATH_ID
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.usersConnectionsRoutes(
    getUsersConnectionsService: GetUsersConnectionsService,
    removeUsersConnectionsService: RemoveUsersConnectionsService
) {
    route(Constants.USERS_CONNECTIONS_ROUTE) {
        authenticate {
            getConnectionsForUser(getUsersConnectionsService)
            removeConnection(removeUsersConnectionsService)
        }
    }
}

fun Route.getConnectionsForUser(usersConnectionsService: GetUsersConnectionsService) {
    get {
        try {
            val userId = call.getUserAuthentication()
            val userResponse = usersConnectionsService.getConnectionsForUser(userId)
            call.respond(HttpStatusCode.OK, userResponse)
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.removeConnection(removeUsersConnectionsService: RemoveUsersConnectionsService) {
    delete(Constants.PATH_ID) {
        try {
            val userId = call.getUserAuthentication()
            val connectedWithUserId = call.parameters[PATH_ID] ?: ""
            val simpleResponse = removeUsersConnectionsService.removeConnectionsForUser(
                userId = userId, connectedWithUserId = connectedWithUserId
            )

            if (simpleResponse.isSuccessful) {
                call.respond(HttpStatusCode.OK, simpleResponse)
            } else {
                call.respond(HttpStatusCode.BadRequest, simpleResponse)
            }
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}