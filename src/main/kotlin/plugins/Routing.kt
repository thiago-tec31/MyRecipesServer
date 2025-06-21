package com.br.plugins

import com.br.application.routes.recipesRoutes
import com.br.application.routes.usersRoute
import com.br.domain.services.recipes.CreateRecipeService
import com.br.domain.services.recipes.FindUserRecipesService
import com.br.domain.services.recipes.GetRecipeByIdService
import com.br.domain.services.recipes.GetUserRecipesService
import com.br.domain.services.recipes.UpdateRecipeService
import com.br.domain.services.users.RegisterUserService
import com.br.domain.services.users.GetProfileUserService
import com.br.domain.services.users.LoginUserService
import io.ktor.server.application.*
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

    install(Routing) {
        usersRoute(registerUserService, loginUserService, getProfileUserService)
        recipesRoutes(
            createRecipeService,
            getUserRecipesService,
            findUserRecipesService,
            getRecipeByIdService,
            updateRecipeService
        )
    }
}
