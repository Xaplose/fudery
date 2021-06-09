package bangkit.xaplose.fudery.data.source.remote

import bangkit.xaplose.fudery.data.source.remote.network.ApiService
import bangkit.xaplose.fudery.data.source.remote.network.PostRetrofitInstance
import bangkit.xaplose.fudery.data.source.remote.response.FoodPredictionResponse
import bangkit.xaplose.fudery.data.source.remote.response.IngredientResponse
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class RemoteDataSource(private val apiService: ApiService) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiService).apply { instance = this }
            }
    }

    suspend fun getFoodListByName(name: String): IngredientSearchResponse {
        return apiService.getFoodListByName(name)
    }

    suspend fun getFoodById(id: Int): IngredientResponse {
        return apiService.getFoodById(id)
    }

    suspend fun predict(imgFilePath: String): FoodPredictionResponse {
        val postApi = PostRetrofitInstance.postApi

        val imgFile = File(imgFilePath)
        val requestFile = imgFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", imgFile.name, requestFile)

        return postApi.getPrediciton(body)
    }
}

