package com.br.plugins

import com.br.application.routes.recipesRoutes
import com.br.application.routes.usersConnectionsRoutes
import com.br.application.routes.usersRoute
import com.br.application.routes.webSocketsRoutes
import com.br.domain.services.connection.ConnectionSessionService
import com.br.domain.services.qrcode.QrCodeGeneratorService
import com.br.domain.services.qrcode.QrCodeReaderService
import com.br.domain.services.recipes.CreateRecipeService
import com.br.domain.services.recipes.DeleteRecipeService
import com.br.domain.services.recipes.FindUserRecipesService
import com.br.domain.services.recipes.GetRecipeByIdService
import com.br.domain.services.recipes.GetUserRecipesService
import com.br.domain.services.recipes.UpdateRecipeService
import com.br.domain.services.users.RegisterUserService
import com.br.domain.services.users.GetProfileUserService
import com.br.domain.services.users.LoginUserService
import com.br.domain.services.usersconnections.AddUsersConnectionsService
import com.br.domain.services.usersconnections.GetUsersConnectionsService
import com.br.domain.services.usersconnections.RemoveUsersConnectionsService
import com.br.util.Constants
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val registerUserService by inject<RegisterUserService>()
    val loginUserService by inject<LoginUserService>()
    val getProfileUserService by inject<GetProfileUserService>()

    val createRecipeService by inject<CreateRecipeService>()
    val getUserRecipesService by inject<GetUserRecipesService>()
    val findUserRecipesService by inject<FindUserRecipesService>()
    val getRecipeByIdService by inject<GetRecipeByIdService>()
    val updateRecipeService by inject<UpdateRecipeService>()
    val deleteRecipeService by inject<DeleteRecipeService>()

    val getUsersConnectionsService by inject<GetUsersConnectionsService>()
    val removeUsersConnectionsService by inject<RemoveUsersConnectionsService>()

    val qrCodeReaderService by inject<QrCodeReaderService>()
    val connectionSessionService by inject<ConnectionSessionService>()
    val qrCodeGeneratorService by inject<QrCodeGeneratorService>()
    val addUsersConnectionsService by inject<AddUsersConnectionsService>()


    routing {
        usersRoute(registerUserService, loginUserService, getProfileUserService)
        recipesRoutes(
            createRecipeService,
            getUserRecipesService,
            findUserRecipesService,
            getRecipeByIdService,
            updateRecipeService,
            deleteRecipeService
        )
        usersConnectionsRoutes(
            getUsersConnectionsService,
            removeUsersConnectionsService
        )
        webSocketsRoutes(
            qrCodeReaderService,
            connectionSessionService,
            qrCodeGeneratorService,
            addUsersConnectionsService
        )

        swaggerUI(path = "api/v1/users_swagger", swaggerFile = "openapi/users_documentation.yaml")
        swaggerUI(path = "api/v1/recipes_swagger", swaggerFile = "openapi/recipes_documentation.yaml")
        swaggerUI(path = "api/v1/usersConnections_swagger", swaggerFile = "openapi/usersConnections_documentation.yaml")
    }

}
