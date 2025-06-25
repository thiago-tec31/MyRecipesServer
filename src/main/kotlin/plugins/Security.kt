package com.br.plugins

import com.br.domain.services.token.TokenService
import com.br.domain.services.users.GetUserByIdService
import io.ktor.server.application.*
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val getUserByIdService by inject<GetUserByIdService>()
    val tokenService by inject<TokenService>()

    authentication {
        jwt {
            verifier(tokenService.verifier())
            realm = tokenService.realm()
            validate { jwtCredential ->
                if (jwtCredential.audience.contains(tokenService.audience())) {
                    val subject = jwtCredential.subject
                    if (!subject.isNullOrEmpty()) {
                        getUserByIdService.getUserById(subject) ?: throw IllegalArgumentException("Usuário não encontrado")
                    } else {
                        throw IllegalArgumentException("Subject é nulo ou vazio")
                    }
                } else {
                    throw IllegalArgumentException("Audience não foi encontrado")
                }
            }
        }
    }
}
