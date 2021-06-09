package bangkit.xaplose.fudery.data

import androidx.lifecycle.LiveData
import bangkit.xaplose.fudery.data.model.Food
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.data.model.FoodPrediction
import bangkit.xaplose.fudery.data.source.local.room.FoodDao
import bangkit.xaplose.fudery.data.source.remote.RemoteDataSource
import bangkit.xaplose.fudery.data.source.remote.response.CaloricBreakdown
import bangkit.xaplose.fudery.data.source.remote.response.IngredientResponse
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse
import bangkit.xaplose.fudery.data.source.remote.response.WeightPerServing
import bangkit.xaplose.fudery.utils.Constants.Companion.INGREDIENTS_IMAGE_BASE_URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository(
    private val dataSource: RemoteDataSource,
    private val mFoodDao: FoodDao
) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    suspend fun getFoodListByName(name: String): ArrayList<Food> {
        var foodList: ArrayList<Food> = arrayListOf()
        try {
            val ingredientSearchResponse: IngredientSearchResponse =
                dataSource.getFoodListByName(name)
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
        } catch (e: Exception) {
            if (e.message.toString().replace("\\s".toRegex(), "") == "HTTP402") {
                foodList.add(
                    Food(
                        -1,
                        "API limit exceeded, please try again in 24 hours.",
                        INGREDIENTS_IMAGE_BASE_URL + "error.jpg"
                    )
                )
            } else {
                foodList.add(
                    Food(
                        -1,
                        "Something went wrong, please try again in a few moments.",
                        INGREDIENTS_IMAGE_BASE_URL + "error.jpg"
                    )
                )
            }

        }
        return foodList
    }

    suspend fun getFoodById(id: Int): FoodDetails {
        try {
            val ingredientResponse = dataSource.getFoodById(id)
            return FoodDetails(
                ingredientResponse.id,
                ingredientResponse.name,
                INGREDIENTS_IMAGE_BASE_URL + ingredientResponse.image,
                ingredientResponse.nutrition.weightPerServing,
                ingredientResponse.nutrition.caloricBreakdown,
                ingredientResponse.nutrition.nutrients
            )
        } catch (e: Exception) {
            if (e.message.toString().replace("\\s".toRegex(), "") == "HTTP402") {
                return FoodDetails(
                    -1,
                    "API Limit Exceeded",
                    INGREDIENTS_IMAGE_BASE_URL + "error.jpg",
                    WeightPerServing(0, ""),
                    CaloricBreakdown(0.0, 0.0, 0.0),
                    emptyList()
                )
            } else {
                return FoodDetails(
                    -1,
                    "Something went wrong",
                    INGREDIENTS_IMAGE_BASE_URL + "error.jpg",
                    WeightPerServing(0, ""),
                    CaloricBreakdown(0.0, 0.0, 0.0),
                    emptyList()
                )
            }
        }
    }

    fun getAllFoodHistory(): LiveData<List<FoodDetails>> = mFoodDao.getAllFood()

    fun getFoodHistoryById(id: Int): LiveData<FoodDetails> = mFoodDao.getFoodById(id)

    fun insert(food: FoodDetails) {
        executorService.execute { mFoodDao.insert(food) }
    }

    fun delete(food: FoodDetails) {
        executorService.execute { mFoodDao.delete(food) }
    }

    suspend fun predict(imgFilePath: String): FoodPrediction {
        val foodPredictionResponse = dataSource.predict(imgFilePath)
        return foodPredictionResponse.foodPredictionList[0]
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(dataSource: RemoteDataSource, foodDao: FoodDao): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(dataSource, foodDao).apply { instance = this }
            }
    }
}