package com.br.domain.services.recipes

import com.br.application.mappers.toRecipesResponse
import com.br.application.payloads.responses.RecipesResponse
import com.br.infra.repository.recipes.RecipesReadOnlyRepository
import com.br.infra.repository.usersconnections.UsersConnectionReadOnlyRepository

class FindUserRecipesService(
    private val recipesReadOnlyRepository: RecipesReadOnlyRepository,
    private val usersConnectionReadOnlyRepository: UsersConnectionReadOnlyRepository
) {
    suspend fun search(nameOrIngredient: String, userId: String): List<RecipesResponse> {
        if (nameOrIngredient.isNotEmpty() || nameOrIngredient.isNotBlank()){
            val usersConnections = usersConnectionReadOnlyRepository.getUsersConnection(userId)
            val userRecipes = recipesReadOnlyRepository.search(
                nameOrIngredient = nameOrIngredient,
                userId = userId
            )

            val sharedRecipes = usersConnections.flatMap {
                recipesReadOnlyRepository.search(
                    nameOrIngredient = nameOrIngredient,
                    userId = it.connectedWithUserId
                )
            }

            return (userRecipes + sharedRecipes).map { it.toRecipesResponse() }
        } else {
            return emptyList()
        }
    }
}