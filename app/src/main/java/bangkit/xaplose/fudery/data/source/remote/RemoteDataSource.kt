package bangkit.xaplose.fudery.data.source.remote

import bangkit.xaplose.fudery.data.source.remote.network.ApiService
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse

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
}

