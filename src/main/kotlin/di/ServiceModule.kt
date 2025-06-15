package com.br.di

import com.br.domain.services.user.AddUserService
import org.koin.dsl.module

object ServiceModule {
    val module = module {
        single<AddUserService> { AddUserService(get(), get()) }
    }
}