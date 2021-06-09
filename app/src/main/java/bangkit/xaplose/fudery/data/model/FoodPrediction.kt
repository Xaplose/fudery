package bangkit.xaplose.fudery.data.model

import com.google.gson.annotations.SerializedName

data class FoodPrediction(
    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("accuracy")
    val accuracy: String
)