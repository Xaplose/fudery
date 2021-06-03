package bangkit.xaplose.fudery.data.source.remote.network

import bangkit.xaplose.fudery.BuildConfig
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search?apiKey=${BuildConfig.API_KEY}")
    suspend fun getFoodListByName(
        @Query("query") query: String
    ): IngredientSearchResponse
}
