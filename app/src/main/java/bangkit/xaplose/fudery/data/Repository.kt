package bangkit.xaplose.fudery.data

import bangkit.xaplose.fudery.data.model.Food
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.data.source.remote.RemoteDataSource
import bangkit.xaplose.fudery.data.source.remote.response.IngredientResponse
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse
import bangkit.xaplose.fudery.utils.Constants.Companion.INGREDIENTS_IMAGE_BASE_URL

class Repository(private val dataSource: RemoteDataSource) {

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(dataSource: RemoteDataSource): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(dataSource).apply { instance = this }
            }
    }

    suspend fun getFoodListByName(name: String): ArrayList<Food> {
        var foodList: ArrayList<Food> = arrayListOf()
        val ingredientSearchResponse: IngredientSearchResponse = dataSource.getFoodListByName(name)
        val ingredientListResponse: List<IngredientResponse> =
            ingredientSearchResponse.ingredientList
        ingredientListResponse.forEach { ingredientResponse ->
            foodList.add(
                Food(
                    ingredientResponse.id,
                    ingredientResponse.name,
                    INGREDIENTS_IMAGE_BASE_URL + ingredientResponse.image
                )
            )
        }
        return foodList
    }

    suspend fun getFoodById(id: Int): FoodDetails {
        val ingredientResponse = dataSource.getFoodById(id)
        val foodDetails = FoodDetails(
            ingredientResponse.id,
            ingredientResponse.name,
            INGREDIENTS_IMAGE_BASE_URL + ingredientResponse.image,
            ingredientResponse.nutrition.weightPerServing,
            ingredientResponse.nutrition.caloricBreakdown,
            ingredientResponse.nutrition.nutrients
        )
        return foodDetails
    }
}