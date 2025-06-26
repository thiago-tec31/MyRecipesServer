package com.br.di

import com.br.infra.repository.qrcode.QrCodeReadOnlyRepository
import com.br.infra.repository.qrcode.QrCodeRepository
import com.br.infra.repository.qrcode.QrCodeWriteOnlyRepository
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.recipes.RecipesRepository
import com.br.infra.repository.recipes.RecipesWriteOnlyRepository
import com.br.infra.repository.users.UserReadOnlyRepository
import com.br.infra.repository.users.UserRepository
import com.br.infra.repository.users.UserWriteOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionWriteOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionRepository
import org.koin.dsl.module

object RepositoryModule {
    val module = module {
        single<UserWriteOnlyRepository> { UserRepository(get()) }
        single<UserReadOnlyRepository> { UserRepository(get()) }

        single<RecipesWriteOnlyRepository> { RecipesRepository(get()) }
        single<RecipesReadOnlyRepository> { RecipesRepository(get()) }

        single<UsersConnectionWriteOnlyRepository> { UsersConnectionRepository(get()) }
        single<UsersConnectionReadOnlyRepository> { UsersConnectionRepository(get()) }

        single<QrCodeReadOnlyRepository> { QrCodeRepository(get()) }
        single<QrCodeWriteOnlyRepository> { QrCodeRepository(get()) }
    }
}