package bangkit.xaplose.fudery.data.source.local.entityConverter

import androidx.room.TypeConverter
import bangkit.xaplose.fudery.data.source.remote.response.CaloricBreakdown

class CaloricBreakdownConverter {
    @TypeConverter
    fun fromString(stringCalBdown: String): CaloricBreakdown {
        val calBdownData = stringCalBdown.split("-")
        return CaloricBreakdown(
            calBdownData[0].toDouble(),
            calBdownData[1].toDouble(),
            calBdownData[2].toDouble()
        )
    }

    @TypeConverter
    fun toString(calBdown: CaloricBreakdown): String {
        return "${calBdown.percentCarbs}-${calBdown.percentProtein}-${calBdown.percentFat}"
    }
}