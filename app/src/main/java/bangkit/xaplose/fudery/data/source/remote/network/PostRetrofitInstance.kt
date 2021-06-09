package bangkit.xaplose.fudery.data.source.remote.network

import bangkit.xaplose.fudery.utils.Constants.Companion.IMAGE_PREDICTION_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostRetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(IMAGE_PREDICTION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val postApi: PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}