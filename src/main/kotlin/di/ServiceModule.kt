package com.br.di

import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.services.recipes.CreateRecipeService
import com.br.domain.services.recipes.FindUserRecipesService
import com.br.domain.services.recipes.GetRecipeByIdService
import com.br.domain.services.recipes.GetUserRecipesService
import com.br.domain.services.recipes.UpdateRecipeService
import com.br.domain.services.token.TokenService
import com.br.domain.services.users.RegisterUserService
import com.br.domain.services.users.GetProfileUserService
import com.br.domain.services.users.GetUserByIdService
import com.br.domain.services.users.LoginUserService
import org.koin.dsl.module

object ServiceModule {
    val module = module {
        single<RegisterUserService> {
            RegisterUserService(
                addValidationUserRequest = get(),
                bCryptPasswordService = get(),
                userWriteOnlyRepository = get(),
                userReadOnlyRepository = get()
            )
        }
        single<LoginUserService> {
            LoginUserService(
                tokenService = get(),
                authValidationUserRequest = get(),
                bCryptPasswordService = get(),
                userReadOnlyRepository = get()
            )
        }

        single<GetProfileUserService> { GetProfileUserService(get()) }
        single<GetUserByIdService> { GetUserByIdService(get()) }

        single<TokenService> { TokenService(get()) }
        single<BCryptPasswordService> { BCryptPasswordService() }

        single<CreateRecipeService> { CreateRecipeService(get(), get()) }

        single<GetUserRecipesService> { GetUserRecipesService(get()) }

        single<FindUserRecipesService> { FindUserRecipesService(get()) }

        single<GetRecipeByIdService> { GetRecipeByIdService(get()) }

        single<UpdateRecipeService> {
            UpdateRecipeService(
                recipesReadOnlyRepository = get(),
                recipesWriteOnlyRepository = get(),
                validationRecipeRequest = get()
            )
        }
    }
}