package bangkit.xaplose.fudery.data

import androidx.lifecycle.LiveData
import bangkit.xaplose.fudery.data.model.Food
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.data.source.local.room.FoodDao
import bangkit.xaplose.fudery.data.source.remote.RemoteDataSource
import bangkit.xaplose.fudery.data.source.remote.response.IngredientResponse
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse
import bangkit.xaplose.fudery.utils.Constants.Companion.INGREDIENTS_IMAGE_BASE_URL
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository(
    private val dataSource: RemoteDataSource,
    private val mFoodDao: FoodDao
) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

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
        return FoodDetails(
            ingredientResponse.id,
            ingredientResponse.name,
            INGREDIENTS_IMAGE_BASE_URL + ingredientResponse.image,
            ingredientResponse.nutrition.weightPerServing,
            ingredientResponse.nutrition.caloricBreakdown,
            ingredientResponse.nutrition.nutrients
        )
    }

    fun getAllFoodHistory(): LiveData<List<FoodDetails>> = mFoodDao.getAllFood()

    fun getFoodHistoryById(id: Int): LiveData<FoodDetails> = mFoodDao.getFoodById(id)

    fun insert(food: FoodDetails) {
        executorService.execute { mFoodDao.insert(food) }
    }

    fun delete(food: FoodDetails) {
        executorService.execute { mFoodDao.delete(food) }
    }

    fun predict(imgFilePath: String) = dataSource.predict(imgFilePath)

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(dataSource: RemoteDataSource, foodDao: FoodDao): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(dataSource, foodDao).apply { instance = this }
            }
    }
}