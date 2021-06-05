package bangkit.xaplose.fudery.data.model

import bangkit.xaplose.fudery.data.source.remote.response.CaloricBreakdown
import bangkit.xaplose.fudery.data.source.remote.response.NutrientsItem
import bangkit.xaplose.fudery.data.source.remote.response.WeightPerServing

data class FoodDetails(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val weightPerServing: WeightPerServing,
    val caloricBreakdown: CaloricBreakdown,
    val nutrients: List<NutrientsItem>
)