package application.config

import io.ktor.server.config.ApplicationConfig

class AppConfig {
    val applicationConfig: ApplicationConfig = ApplicationConfig("application.conf")
}
