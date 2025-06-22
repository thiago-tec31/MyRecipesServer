package com.br.plugins

import com.br.domain.services.token.TokenService
import com.br.domain.services.users.GetUserByIdService
import io.ktor.server.application.*
import io.ktor.server.auth.Principal
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTCredential
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
                validateCredential(jwtCredential, tokenService, getUserByIdService)
            }
        }
    }
}

suspend fun validateCredential(
    jwtCredential: JWTCredential,
    tokenService: TokenService,
    getUserByIdService: GetUserByIdService
): Principal {
    if (jwtCredential.audience.contains(tokenService.audience())) {
        val subject = jwtCredential.subject
        if (!subject.isNullOrEmpty()) {
            val user = getUserByIdService.getUserById(subject)
            if (user != null) {
                return user
            } else {
                throw IllegalArgumentException("Usuário não encontrado para o ID: $subject")
            }
        } else {
            throw IllegalArgumentException("Subject é nulo ou vazio")
        }
    } else {
        throw IllegalArgumentException("Audience não corresponde")
    }
}
