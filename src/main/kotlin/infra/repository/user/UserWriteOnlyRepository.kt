package com.br.infra.repository.user

import com.br.domain.entity.User

interface UserWriteOnlyRepository {
    suspend fun insertUser(user: User): Boolean
}