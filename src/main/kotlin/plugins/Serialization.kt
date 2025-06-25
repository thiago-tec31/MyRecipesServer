package com.br.plugins

import com.google.gson.Strictness
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            setStrictness(Strictness.LENIENT)
            serializeNulls()
            setPrettyPrinting()
        }
    }
}
