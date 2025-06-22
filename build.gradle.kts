
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "com.br"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.server.metrics)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.openapi)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.kotlin.qrcode)
    implementation(libs.kotlin.datetime)
    implementation(libs.ktor.koin)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.koin.logger)
    implementation(libs.ktor.bcrypt)
    implementation(libs.kotlin.mongodb.courotine)
    testImplementation(libs.ktor.junit5)
    testImplementation(libs.ktor.junit5.api)
    testImplementation(libs.ktor.junit5.engine)
    testImplementation(libs.ktor.turbine)
    testImplementation(libs.ktor.mockk)
    testImplementation(libs.ktor.truth)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("unitTest") {
    useJUnitPlatform {
        includeTags("unit")
    }
}

tasks.register<Test>("userRoutesTest") {
    useJUnitPlatform {
        includeTags("integration")
    }
    filter {
        includeTestsMatching("application.routes.UserRoutesTest")
    }
}

tasks.register<Test>("recipesRoutesTest") {
    useJUnitPlatform {
        includeTags("integration")
    }
    filter {
        includeTestsMatching("application.routes.RecipesRoutesTest")
    }
}

