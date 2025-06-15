package com.br.di

import com.br.domain.validations.AddValidationUserRequest
import com.br.domain.validations.AddValidationUserRequestImpl
import org.koin.dsl.module

object ValidationsModule {
    val module = module {
        single<AddValidationUserRequest> { AddValidationUserRequestImpl() }
    }
}