package domain.model

import com.br.domain.entity.CategoryEnum
import com.br.domain.entity.Ingredients
import com.br.domain.entity.Recipes

class RecipesFactory {
    fun create(
        userId: String,
        recipeId: String,
        recipesFactoryFake: RecipesFactoryFake
    ) = when (recipesFactoryFake) {
        RecipesFactoryFake.Anna -> {
            Recipes(
                id = recipeId,
                name = "Lasanha",
                userId = userId,
                category = CategoryEnum.Lunch,
                preparationTime = "120",
                preparationMode = "Cozinhe a massa da lasanha em aproximadamente em 2 litros de água por 5 minutos." +
                        "Em uma panela cozinhe a carne moída, depois de cozida coloque molho de tomate, o sal e temperos a gosto." +
                        "Comece montando com uma camada de molho, a massa da lasanha, o presunto e o queijo" +
                        "Aqueça o forno a 180º C durante 5 minutos." +
                        "Coloque a lasanha no forno de 20 a 30 minutos.",

                ingredients = listOf(
                    Ingredients(
                        name = "massa de lasanha (pronta)",
                        quantity = "1",
                        recipeId = recipeId
                    ),
                    Ingredients(
                        name = "queijo mussarela",
                        quantity = "500g",
                        recipeId = recipeId
                    ),
                    Ingredients(
                        name = "massa de tomate pronta",
                        quantity = "1",
                        recipeId = recipeId
                    )
                )
            )
        }

        RecipesFactoryFake.Alex -> {
            Recipes(
                id = recipeId,
                name = "Strogonoff de frango",
                userId = userId,
                category = com.br.domain.entity.CategoryEnum.Dinner,
                preparationTime = "60",
                preparationMode = "Em uma panela, misture o frango, o alho, a maionese, o sal e a pimenta." +
                        "Em uma frigideira grande, derreta a manteiga e doure a cebola." +
                        "Junte o frango temperado até que esteja dourado." +
                        "Adicione os cogumelos, o ketchup e a mostarda." +
                        "Sirva com arroz branco e batata palha.",

                ingredients = listOf(
                    Ingredients(
                        name = "peitos de frango cortados em cubos",
                        quantity = "3",
                        recipeId = recipeId
                    ),
                    Ingredients(
                        name = "cebola picada",
                        quantity = "1",
                        recipeId = recipeId
                    ),
                    Ingredients(
                        name = "dente de alho picado",
                        quantity = "1",
                        recipeId = recipeId
                    )
                )
            )
        }
    }

    sealed class RecipesFactoryFake {
        data object Anna : RecipesFactoryFake()
        data object Alex : RecipesFactoryFake()
    }

}