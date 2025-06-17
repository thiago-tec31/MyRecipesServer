package com.br.di

import com.br.domain.services.password.BCryptPasswordService
import com.br.domain.services.token.TokenService
import com.br.domain.services.user.RegisterUserService
import com.br.domain.services.user.GetProfileUserService
import com.br.domain.services.user.GetUserByIdService
import com.br.domain.services.user.LoginUserService
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
                authUserRequestValidation = get(),
                bCryptPasswordService = get(),
                userReadOnlyRepository = get()
            )
        }
        single<GetProfileUserService> { GetProfileUserService(get()) }
        single<GetUserByIdService> { GetUserByIdService(get()) }
        single<TokenService> { TokenService(get()) }
        single<BCryptPasswordService> { BCryptPasswordService() }
    }
}