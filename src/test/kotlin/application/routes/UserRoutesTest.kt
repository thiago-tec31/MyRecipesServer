package com.br.application.routes

import application.payloads.requests.RegisterUserRequest
import com.br.application.payloads.requests.AuthUserRequest
import com.br.application.payloads.responses.SimpleResponse
import com.br.application.payloads.responses.TokenResponse
import com.br.application.payloads.responses.UserResponse
import com.google.common.truth.Truth.assertThat
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test

class UserRoutesTest {

    private val applicationConfig = ApplicationConfig("application.conf")

    private val authUserRequest = AuthUserRequest(email = "alex@gmail.com", password = "123456A@")
    private val registerUserRequest = RegisterUserRequest(
        name = "Alex",
        email = "alex@gmail.com",
        password = "123456A@",
        phone = "32 9 9145-3688"
    )

    @Test
    fun register() = testApplication {

        environment { applicationConfig }

        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        val response = client.post("/api/v1/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerUserRequest)
        }

        val simpleResponse = response.body<SimpleResponse>()

        println("SimpleResponse: $simpleResponse")
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        assertThat(simpleResponse.isSuccessful).isTrue()
        assertThat(simpleResponse.message).isNotEmpty()
    }

    @Test
    fun login() = testApplication {

        environment { applicationConfig }

        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        val response = client.post("/api/v1/users/login") {
            contentType(ContentType.Application.Json)
            setBody(registerUserRequest)
        }

        val tokenResponse = response.body<TokenResponse>()

        println("TokenResponse: $tokenResponse")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(tokenResponse.isSuccessful).isTrue()
        assertThat(tokenResponse.token).isNotEmpty()
        assertThat(tokenResponse.message).isNotEmpty()
    }

    @Test
    fun getProfile() = testApplication {

        environment { applicationConfig }

        var client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        val responseLogin = client.post("/api/v1/users/login") {
            contentType(ContentType.Application.Json)
            setBody(registerUserRequest)
        }

        val tokenResponse = responseLogin.body<TokenResponse>()

        client = createClient {
            install(ContentNegotiation) {
                gson()
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            accessToken = tokenResponse.token.orEmpty(),
                            refreshToken = tokenResponse.token.orEmpty()
                        )
                    }
                }
            }
        }

        val response = client.get("/api/v1/users/profile") {
            contentType(ContentType.Application.Json)
            setBody(registerUserRequest)
        }

        val userResponse = response.body<UserResponse>()

        println("UserResponse: $userResponse")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(userResponse).isNotNull()
    }

}