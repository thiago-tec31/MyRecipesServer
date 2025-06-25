package domain.model

import com.br.domain.entity.UsersConnections

class UsersConnectionsFactory {
    fun create(usersConnectionFake: UsersConnectionFake, userId: String, connectedWithUserId: String) =
        when(usersConnectionFake) {
            UsersConnectionFake.One -> {
                UsersConnections(
                    userId = userId,
                    connectedWithUserId = connectedWithUserId
                )
            }
        }

    sealed interface UsersConnectionFake {
        data object One: UsersConnectionFake
    }
}