package com.br.di

import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.recipes.RecipesRepository
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.infra.repository.user.UserReadOnlyRepository
import com.br.infra.repository.user.UserRepository
import com.br.infra.repository.user.UserWriteOnlyRepository
import org.koin.dsl.module

object RepositoryModule {
    val module = module {
        single<UserWriteOnlyRepository> { UserRepository(get()) }
        single<UserReadOnlyRepository> { UserRepository(get()) }

        single<RecipesWriteOnlyRepository> { RecipesRepository(get()) }
        single<RecipesReadOnlyRepository> { RecipesRepository(get()) }
    }
}