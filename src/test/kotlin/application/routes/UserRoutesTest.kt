package application.routes

import com.br.application.payloads.requests.RegisterUserRequest
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
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
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
    @Order(1)
    fun register() = testApplication {

        environment { applicationConfig }

        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        val httpResponse = client.post("/api/v1/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerUserRequest)
        }

        val simpleResponse = httpResponse.body<SimpleResponse>()

        println("SimpleResponse: $simpleResponse")
        assertThat(httpResponse.status).isEqualTo(HttpStatusCode.Created)
        assertThat(simpleResponse.isSuccessful).isTrue()
        assertThat(simpleResponse.message).isNotEmpty()

        client.close()
    }

    @Test
    @Order(2)
    fun login() = testApplication {

        environment { applicationConfig }

        val client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        val httpResponse = client.post("/api/v1/users/login") {
            contentType(ContentType.Application.Json)
            setBody(authUserRequest)
        }

        val tokenResponse = httpResponse.body<TokenResponse>()

        println("TokenResponse: $tokenResponse")
        assertThat(httpResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(tokenResponse.isSuccessful).isTrue()
        assertThat(tokenResponse.token).isNotEmpty()
        assertThat(tokenResponse.message).isNotEmpty()

        client.close()
    }

    @Test
    @Order(3)
    fun getProfile() = testApplication {

        environment { applicationConfig }

        var client = createClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        var httpResponse = client.post("/api/v1/users/login") {
            contentType(ContentType.Application.Json)
            setBody(authUserRequest)
        }

        val tokenResponse = httpResponse.body<TokenResponse>()

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

        httpResponse = client.get("/api/v1/users/profile") {
            contentType(ContentType.Application.Json)
            setBody(registerUserRequest)
        }

        val userResponse = httpResponse.body<UserResponse>()

        println("UserResponse: $userResponse")
        assertThat(httpResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(userResponse).isNotNull()

        client.close()
    }

}