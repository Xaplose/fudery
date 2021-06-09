package bangkit.xaplose.fudery.data.source.local.entityConverter

import androidx.room.TypeConverter
import bangkit.xaplose.fudery.data.source.remote.response.NutrientsItem

class NutrientItemListConverter {
    @TypeConverter
    fun fromString(stringListNutrient: String): ArrayList<NutrientsItem> {
        val nutrientsData = stringListNutrient.split(",").map {
            it.split("-")
        }
        return ArrayList(nutrientsData.map { NutrientsItem(it[0].toDouble(), it[1], it[2], it[3]) })
    }

    @TypeConverter
    fun toString(nutrientList: List<NutrientsItem>): String {
        return nutrientList.joinToString(separator = ",") {
            "${it.amount}-${it.unit}-${it.name}-${it.title}"
        }
    }
}