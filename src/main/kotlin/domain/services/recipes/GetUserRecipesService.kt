package com.br.domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.application.payloads.responses.RecipesResponse
import com.br.domain.entity.CategoryEnum
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.users.UserReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository

class GetUserRecipesService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository,
    private val userReadOnlyRepository: UserReadOnlyRepository,
    private val usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository
) {
    suspend fun getAll(userId: String, category: Int?) : List<RecipesResponse> {
        val recipes = recipesReadOnlyRepository.getByUser(
            userId = userId,
            categoryEnum = category?.let { CategoryEnum.fromInt(it) }
        )

        val usersConnection = usersConnectionReadOnlyRepository.getUsersConnection(userId)

        val connectedUserRecipes = usersConnection.flatMap { connection ->
            recipesReadOnlyRepository.getByUser(userId = connection.connectedWithUserId,
                categoryEnum = category?.let { CategoryEnum.fromInt(it) })
        }

        val allRecipes = recipes + connectedUserRecipes

        val connectedUserIdToNameMap = if (usersConnection.isNotEmpty()) {
            val usersIds = connectedUserRecipes.map { recipe -> recipe.userId }.distinct()

            val users = userReadOnlyRepository.findUsersByIds(usersIds)
            users.associateBy({ it.id  }, { it.name })
        } else {
            emptyMap()
        }


        return allRecipes.map {
            val ownerName = connectedUserIdToNameMap[it.userId]
            it.toRecipesResponse().copy(
                ownerName = ownerName
            )
        }
    }
}