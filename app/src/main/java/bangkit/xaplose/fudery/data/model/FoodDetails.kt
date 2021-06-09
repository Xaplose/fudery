package bangkit.xaplose.fudery.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import bangkit.xaplose.fudery.data.source.remote.response.CaloricBreakdown
import bangkit.xaplose.fudery.data.source.remote.response.NutrientsItem
import bangkit.xaplose.fudery.data.source.remote.response.WeightPerServing

@Entity(tableName = "foodentity")
data class FoodDetails(

    @PrimaryKey
    val id: Int,

    val name: String,
    val imageUrl: String,
    val weightPerServing: WeightPerServing,
    val caloricBreakdown: CaloricBreakdown,
    val nutrients: List<NutrientsItem>
)