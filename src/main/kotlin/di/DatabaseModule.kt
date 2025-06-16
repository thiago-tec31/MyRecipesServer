package com.br.di

import com.br.domain.database.DatabaseService
import org.koin.dsl.module

object DatabaseModule {
    val module  = module {
        single<DatabaseService>{ DatabaseService(get()) }
    }
}