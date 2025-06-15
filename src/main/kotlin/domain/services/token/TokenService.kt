package com.br.domain.services.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.br.util.Constants
import com.br.util.ErrorCodes

class TokenService {

    private val issuer = "MyRecipesServer"
    private val realm = "project.recipes"
    private val audience = "MyRecipesApp"
    private val jwtSecret = System.getenv(Constants.SECRET)
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

    fun retrieveIdByToken(token: String) : String {
        return try {
            val decodedJwt = JWT.require(algorithm)
                .withAudience(audience)
                .withIssuer(issuer)
                .build()
                .verify(token)
            decodedJwt.subject
        } catch (ex: JWTVerificationException) {
            throw IllegalArgumentException(ErrorCodes.INVALID_TOKEN.message)
        }
    }
}