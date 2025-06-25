package com.br.di

import com.br.infra.manager.ConnectionManager
import com.br.infra.manager.ConnectionManagerImpl
import org.koin.dsl.module

object InfraModule {
    val module = module {
        single<ConnectionManager> { ConnectionManagerImpl() }
    }
}