package com.br.di

import com.br.domain.services.connection.ConnectionSessionService
import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.services.qrcode.QrCodeGeneratorService
import com.br.domain.services.qrcode.QrCodeReaderService
import com.br.domain.services.recipes.CreateRecipeService
import com.br.domain.services.recipes.DeleteRecipeService
import com.br.domain.services.recipes.FindUserRecipesService
import com.br.domain.services.recipes.GetRecipeByIdService
import com.br.domain.services.recipes.GetUserRecipesService
import com.br.domain.services.recipes.UpdateRecipeService
import com.br.domain.services.timer.TimerService
import com.br.domain.services.token.TokenService
import com.br.domain.services.users.RegisterUserService
import com.br.domain.services.users.GetProfileUserService
import com.br.domain.services.users.GetUserByIdService
import com.br.domain.services.users.LoginUserService
import com.br.domain.services.usersconnections.AddUsersConnectionsService
import com.br.domain.services.usersconnections.GetUsersConnectionsService
import com.br.domain.services.usersconnections.RemoveUsersConnections
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
        single<GetUserRecipesService> { GetUserRecipesService(
            recipesReadOnlyRepository = get(),
            userReadOnlyRepository = get(),
            usersConnectionReadOnlyRepository = get()
        ) }
        single<FindUserRecipesService> { FindUserRecipesService(get(), get()) }
        single<GetRecipeByIdService> { GetRecipeByIdService(get(), get()) }
        single<UpdateRecipeService> {
            UpdateRecipeService(
                recipesReadOnlyRepository = get(),
                recipesWriteOnlyRepository = get(),
                validationRecipeRequest = get()
            )
        }
        single<DeleteRecipeService> {
            DeleteRecipeService(
                recipesReadOnlyRepository = get(),
                recipesWriteOnlyRepository = get()
            )
        }

        single<AddUsersConnectionsService> {
            AddUsersConnectionsService(get(), get())
        }
        single<GetUsersConnectionsService> {
            GetUsersConnectionsService(get(), get())
        }
        single<RemoveUsersConnections> {
            RemoveUsersConnections(get(), get())
        }

        single<QrCodeGeneratorService> {
            QrCodeGeneratorService(get())
        }
        single<QrCodeReaderService> {
            QrCodeReaderService(
                qrCodeReadOnlyRepository = get(),
                userReadOnlyRepository = get(),
                usersConnectionReadOnlyRepository = get()
            )
        }

        single<TimerService> { TimerService() }

        single<ConnectionSessionService> { ConnectionSessionService(get(), get()) }
    }
}