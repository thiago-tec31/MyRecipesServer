package com.br.application.routes

import com.br.application.payloads.requests.AddUpdateRecipesRequest
import com.br.application.payloads.responses.ErrorResponse
import com.br.domain.extensions.getUserAuthentication
import com.br.domain.services.recipes.CreateRecipeService
import com.br.domain.services.recipes.FindUserRecipesService
import com.br.domain.services.recipes.GetRecipeByIdService
import com.br.domain.services.recipes.GetUserRecipesService
import com.br.domain.services.recipes.UpdateRecipeService
import com.br.util.Constants
import com.br.util.ErrorCodes
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.put

fun Route.recipesRoutes(
    createRecipeService: CreateRecipeService,
    getUserRecipesService: GetUserRecipesService,
    findUserRecipesService: FindUserRecipesService,
    getRecipeByIdService: GetRecipeByIdService,
    updateRecipeService: UpdateRecipeService
) {
    route(Constants.RECIPE_ROUTE) {
        authenticate {
            create(createRecipeService)
            getAll(getUserRecipesService)
            getById(getRecipeByIdService)
            search(findUserRecipesService)
            update(updateRecipeService)
        }
    }
}

fun Route.create(
    createRecipeService: CreateRecipeService
) {
    post {
        val userId = call.getUserAuthentication()
        try {
            val request = call.receiveNullable<AddUpdateRecipesRequest>()
            if (request != null) {
                val simpleResponse = createRecipeService.createRecipe(request, userId)
                if (simpleResponse.isSuccessful) {
                    call.respond(HttpStatusCode.Created, simpleResponse)
                } else {
                    call.respond(HttpStatusCode.BadRequest, simpleResponse)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.getAll(
    getUserRecipesService: GetUserRecipesService
) {
    get {
        val userId = call.getUserAuthentication()
        try {
            val category = call.parameters[Constants.PARAM_CATEGORY]?.toIntOrNull()
            val recipes = getUserRecipesService.getAll(userId, category)
            call.respond(HttpStatusCode.OK, recipes)
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.getById(
    getRecipeByIdService: GetRecipeByIdService
) {
    get(Constants.PATH_ID) {
        val userId = call.getUserAuthentication()
        val recipeId = call.parameters[Constants.PARAM_ID]
        if (recipeId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorCodes.INVALID_PARAMETERS.message)
            return@get
        }

        try {
            val recipeDetailResponse = getRecipeByIdService.getRecipeById(recipeId, userId)
            recipeDetailResponse?.let { safeResponse ->
                call.respond(HttpStatusCode.OK, safeResponse)
            } ?: call.respond(
                ErrorResponse(
                    httpStatusCode = HttpStatusCode.NotFound.value,
                    message = ErrorCodes.NO_RECIPE_FOUND.message
                )
            )
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.search(
    findUserRecipesService: FindUserRecipesService
) {
    get(Constants.PATH_SEARCH) {
        val userId = call.getUserAuthentication()
        val nameOrIngredient = call.parameters[Constants.PARAM_NAME_OR_INGREDIENTS]
        if (nameOrIngredient == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorCodes.INVALID_PARAMETERS.message)
            return@get
        }

        try {
            val recipes = findUserRecipesService.search(nameOrIngredient = nameOrIngredient, userId = userId)
            call.respond(HttpStatusCode.OK, recipes)
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

fun Route.update(
    updateRecipeService: UpdateRecipeService
) {
    put(Constants.PATH_SEARCH) {
        val userId = call.getUserAuthentication()
        val recipeId = call.parameters[Constants.PATH_ID]
        if (recipeId == null) {
            call.respond(HttpStatusCode.BadRequest, ErrorCodes.INVALID_PARAMETERS.message)
            return@put
        }

        try {
            val request = call.receiveNullable<AddUpdateRecipesRequest>()
            request?.let { safeRequest ->
                val simpleResponse = updateRecipeService.update(
                    userId = userId,
                    recipeId = recipeId,
                    addUpdateRecipesRequest = safeRequest
                )

                if (simpleResponse.isSuccessful) {
                    call.respond(HttpStatusCode.OK, simpleResponse)
                } else {
                    call.respond(HttpStatusCode.BadRequest, simpleResponse)
                }
            } ?: call.respond(
                ErrorResponse(
                    httpStatusCode = HttpStatusCode.BadRequest.value,
                    message = ErrorCodes.INVALID_REQUEST_ERROR.message
                )
            )
        } catch (e: ServerResponseException) {
            application.log.error(e.message)
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}