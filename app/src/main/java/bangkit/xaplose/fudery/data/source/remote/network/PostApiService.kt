package bangkit.xaplose.fudery.data.source.remote.network

import bangkit.xaplose.fudery.BuildConfig
import bangkit.xaplose.fudery.data.source.remote.response.IngredientResponse
import bangkit.xaplose.fudery.data.source.remote.response.IngredientSearchResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.*

interface PostApiService {

    @Multipart
    @POST("predict")
    fun getPrediciton(
        @Part("file") photo: MultipartBody.Part
    ): Response
}
