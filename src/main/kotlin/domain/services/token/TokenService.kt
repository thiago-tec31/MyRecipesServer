package com.br.domain.services.token

import application.config.AppConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.br.util.Constants
import com.br.util.ErrorCodes

class TokenService(
    appConfig: AppConfig
) {

    private val issuer = appConfig.applicationConfig.property("jwt.issuer").toString()
    private val realm = appConfig.applicationConfig.property("jwt.realm").toString()
    private val audience = appConfig.applicationConfig.property("jwt.audience").toString()
    private val jwtSecret = appConfig.applicationConfig.property("jwt.secret").toString()
    private val algorithm = Algorithm.HMAC256(jwtSecret)

    private val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun realm() = realm
    fun audience() = audience
    fun verifier(): JWTVerifier = verifier

    fun generateToken(userId: String) : String {
        return try {
            JWT.create()
                .withSubject(userId)
                .withAudience(audience)
                .withIssuer(issuer)
                .sign(algorithm)
        } catch (ex: JWTVerificationException) {
            throw IllegalArgumentException(ErrorCodes.TOKEN_GENERATION_ERROR.message)
        }
    }
}