package com.br.plugins

import com.br.di.ConfigModule
import com.br.di.DatabaseModule
import com.br.di.RepositoryModule
import com.br.di.ServiceModule
import com.br.di.ValidationsModule
import com.br.di.InfraModule
import com.br.domain.services.database.DatabaseService
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    val modules = ConfigModule.module +
            DatabaseModule.module +
            RepositoryModule.module +
            ServiceModule.module +
            ValidationsModule.module +
            InfraModule.module

    install(Koin) {
        modules(modules)
    }

    val databaseService by inject<DatabaseService>()

    environment.monitor.subscribe(ApplicationStopped) {
        databaseService.close()
    }
}