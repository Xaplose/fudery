package bangkit.xaplose.fudery.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class IngredientSearchResponse(
    @field:SerializedName("offset")
    val offset: String,

    @field:SerializedName("number")
    val number: String,

    @field:SerializedName("totalResults")
    val totalResults: String,

    @field:SerializedName("results")
    val ingredientList: List<IngredientResponse>
)