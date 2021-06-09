package bangkit.xaplose.fudery.data.source.local.entityConverter

import androidx.room.TypeConverter
import bangkit.xaplose.fudery.data.source.remote.response.WeightPerServing

class WeightPerServingConverter {
    @TypeConverter
    fun fromString(stringWps: String): WeightPerServing {
        val wpsData = stringWps.split("-")
        return WeightPerServing(wpsData[0].toInt(), wpsData[1])
    }

    @TypeConverter
    fun toString(wps: WeightPerServing): String {
        return "${wps.amount}-${wps.unit}"
    }
}