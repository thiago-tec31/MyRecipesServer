package com.br.domain.model

import com.br.domain.entity.User

class UserFactory {

    fun create(user: UserFake) = when(user) {
        UserFake.Alex -> {
            User(
                name = "Anna",
                email = "anna@gmail.com",
                password = "123456A@",
                phone = "32 9 9180-4040"
            )
        }
        UserFake.Anna -> {
            User(
                name = "Alex",
                email = "alex@gmail.com",
                password = "123456A@",
                phone = "32 9 9180-5151"
            )
        }
    }

    sealed interface UserFake {
        data object Anna: UserFake
        data object Alex: UserFake
    }
}