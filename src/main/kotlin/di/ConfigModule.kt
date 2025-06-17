package com.br.di

import application.config.AppConfig

import org.koin.dsl.module

object ConfigModule {
    val module = module {
        single<AppConfig> { AppConfig() }
    }
}