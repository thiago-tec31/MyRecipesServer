package com.br.application.mappers

import com.br.application.payloads.responses.UserResponse
import com.br.domain.entity.User

fun User?.toUserResponse(): UserResponse {
    return UserResponse(
        id = this?.id.orEmpty(),
        name = this?.name.orEmpty(),
        email = this?.email.orEmpty(),
        phone = this?.phone.orEmpty(),
        createdAt = this?.createdAt.orEmpty(),
    )
}