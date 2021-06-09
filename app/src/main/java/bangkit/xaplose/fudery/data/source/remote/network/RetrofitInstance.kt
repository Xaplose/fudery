package bangkit.xaplose.fudery.data.source.remote.network

import bangkit.xaplose.fudery.utils.Constants.Companion.FOOD_INGREDIENTS_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(FOOD_INGREDIENTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}