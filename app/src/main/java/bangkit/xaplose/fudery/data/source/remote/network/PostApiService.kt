package bangkit.xaplose.fudery.data.source.remote.network

import bangkit.xaplose.fudery.data.source.remote.response.FoodPredictionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostApiService {

    @Multipart
    @POST("predict")
    suspend fun getPrediciton(
        @Part photo: MultipartBody.Part
    ): FoodPredictionResponse
}
