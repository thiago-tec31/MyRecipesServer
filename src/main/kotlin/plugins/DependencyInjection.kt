package com.br.plugins

import com.br.di.DatabaseModule
import com.br.di.RepositoryModule
import com.br.di.ServiceModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    val modules = DatabaseModule.module +
            RepositoryModule.module +
            ServiceModule.module

    install(Koin) {
        modules(modules)
    }
}