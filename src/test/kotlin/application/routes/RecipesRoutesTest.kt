package application.routes

import application.payloads.requests.AddUpdateRecipesRequestFactory
import com.br.application.payloads.requests.AuthUserRequest
import com.br.application.payloads.responses.RecipeDetailResponse
import com.br.application.payloads.responses.RecipesResponse
import com.br.application.payloads.responses.SimpleResponse
import com.br.application.payloads.responses.TokenResponse
import com.google.common.truth.Truth.assertThat
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder

@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RecipesRoutesTest {

    private val applicationConfig = ApplicationConfig("application.conf")
    private val authUserRequest = AuthUserRequest(email = "alex@gmail.com", password = "123456A@")
    private val addRecipeRequest = AddUpdateRecipesRequestFactory().create()
    private val updateRecipeRequest = AddUpdateRecipesRequestFactory().update()

    @Test
    @Order(1)
    fun create() = testApplication {

        environment { config = applicationConfig }

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

        httpResponse = client.post("/api/v1/recipes") {
            contentType(ContentType.Application.Json)
            setBody(addRecipeRequest)
        }

        val responseBody = httpResponse.body<SimpleResponse>()

        assertThat(httpResponse.status).isEqualTo(HttpStatusCode.Created)
        assertThat(responseBody.isSuccessful).isTrue()

        client.close()
    }

    @Test
    @Order(2)
    fun getAll() = testApplication {

        environment { config = applicationConfig }

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

        httpResponse = client.get("/api/v1/recipes") {
            contentType(ContentType.Application.Json)
        }

        val responseText = httpResponse.bodyAsText()
        println("ResponseText: $responseText")

        val responseBody = httpResponse.body<List<RecipesResponse>>()

        assertThat(responseBody).isNotNull()

        client.close()
    }

    @Test
    @Order(3)
    fun getById() = testApplication {

        environment { config = applicationConfig }

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

        httpResponse = client.get("/api/v1/recipes") {
            contentType(ContentType.Application.Json)
        }

        val result = httpResponse.body<List<RecipesResponse>>()
        val recipeId = result.first().id

        httpResponse = client.get("/api/v1/recipes/${recipeId}") {
            contentType(ContentType.Application.Json)
        }

        val responseBody = httpResponse.body<RecipeDetailResponse>()

        val responseText = httpResponse.bodyAsText()
        println("ResponseText: $responseText")

        assertThat(httpResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(responseBody).isNotNull()

        client.close()
    }

    @Test
    @Order(4)
    fun search() = testApplication {

        environment { config = applicationConfig }

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

        httpResponse = client.get("/api/v1/recipes/search") {
            contentType(ContentType.Application.Json)
            parameter("nameOrIngredient", "lasa")
        }

        val responseText = httpResponse.bodyAsText()
        println("ResponseText: $responseText")

        val responseBody = httpResponse.body<List<RecipesResponse>>()

        assertThat(responseBody).isNotEmpty()

        client.close()
    }

    @Test
    @Order(5)
    fun update() = testApplication {

        environment { config = applicationConfig }

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

        httpResponse = client.get("/api/v1/recipes") {
            contentType(ContentType.Application.Json)
        }

        val result = httpResponse.body<List<RecipesResponse>>()
        val recipeId = result.first().id

        httpResponse = client.put("/api/v1/recipes/${recipeId}") {
            contentType(ContentType.Application.Json)
            setBody(updateRecipeRequest)
        }

        val responseBody = httpResponse.body<SimpleResponse>()

        val responseText = httpResponse.bodyAsText()
        println("ResponseText: $responseText")

        assertThat(httpResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(responseBody).isNotNull()

        client.close()
    }

    @Test
    @Order(6)
    fun delete() = testApplication {

        environment { config = applicationConfig }

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

        httpResponse = client.get("/api/v1/recipes") {
            contentType(ContentType.Application.Json)
        }

        val result = httpResponse.body<List<RecipesResponse>>()
        val recipeId = result.first().id

        httpResponse = client.delete("/api/v1/recipes/${recipeId}") {
            contentType(ContentType.Application.Json)
        }

        val responseBody = httpResponse.body<SimpleResponse>()

        val responseText = httpResponse.bodyAsText()
        println("ResponseText: $responseText")

        assertThat(httpResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(responseBody).isNotNull()

        client.close()
    }

}