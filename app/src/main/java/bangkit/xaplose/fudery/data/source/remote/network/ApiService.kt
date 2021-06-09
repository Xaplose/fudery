package bangkit.xaplose.fudery.data.source.remote.network

import bangkit.xaplose.fudery.BuildConfig
import bangkit.xaplose.fudery.data.source.remote.response.IngredientResponse
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search?apiKey=${BuildConfig.API_KEY}")
    suspend fun getFoodListByName(
        @Query("query") query: String
    ): IngredientSearchResponse

    @GET("{id}/information?amount=1&apiKey=${BuildConfig.API_KEY}")
    suspend fun getFoodById(
        @Path("id") id: Int
    ): IngredientResponse


}
