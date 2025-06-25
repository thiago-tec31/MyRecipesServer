package com.br

import com.br.domain.services.database.DatabaseService
import com.br.plugins.configureDependencyInjection
import com.br.plugins.configureHTTP
import com.br.plugins.configureMonitoring
import com.br.plugins.configureRouting
import com.br.plugins.configureSecurity
import com.br.plugins.configureSerialization
import com.br.plugins.configureSockets
import com.br.plugins.configureStatusPage
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import kotlin.getValue

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val databaseService by inject<DatabaseService>()

    monitor.subscribe(ApplicationStopped) {
        databaseService.close()
    }

    configureDependencyInjection()
    configureSecurity()
    configureHTTP()
    configureStatusPage()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting()
}
