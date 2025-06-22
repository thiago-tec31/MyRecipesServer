package com.br.di

import com.br.domain.validations.AddValidationUserRequest
import com.br.domain.validations.AddValidationUserRequestImpl
import com.br.domain.validations.AuthValidationUserRequest
import com.br.domain.validations.AuthValidationUserRequestImpl
import com.br.domain.validations.AddValidationRecipeRequest
import com.br.domain.validations.AddValidationRecipeRequestImpl
import org.koin.dsl.module

object ValidationsModule {
    val module = module {
        single<AddValidationUserRequest> { AddValidationUserRequestImpl() }
        single<AuthValidationUserRequest> { AuthValidationUserRequestImpl() }
        single<AddValidationRecipeRequest> { AddValidationRecipeRequestImpl() }
    }
}