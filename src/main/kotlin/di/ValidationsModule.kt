package com.br.di

import com.br.domain.validations.AddValidationUserRequest
import com.br.domain.validations.AddValidationUserRequestImpl
import com.br.domain.validations.AuthUserRequestValidation
import com.br.domain.validations.AuthUserRequestValidationImpl
import org.koin.dsl.module

object ValidationsModule {
    val module = module {
        single<AddValidationUserRequest> { AddValidationUserRequestImpl() }
        single<AuthUserRequestValidation> { AuthUserRequestValidationImpl() }
    }
}