package com.br.infra.repository.user

import com.br.domain.entity.Users

interface UserWriteOnlyRepository {
    suspend fun insertUser(users: Users): Boolean
}