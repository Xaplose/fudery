package bangkit.xaplose.fudery.data.source.remote.response

import bangkit.xaplose.fudery.data.model.FoodPrediction
import com.google.gson.annotations.SerializedName

data class FoodPredictionResponse(
    @field:SerializedName("results")
    val foodPredictionList: List<FoodPrediction>
)