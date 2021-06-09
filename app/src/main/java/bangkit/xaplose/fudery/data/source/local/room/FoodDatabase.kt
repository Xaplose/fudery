package bangkit.xaplose.fudery.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.data.source.local.entityConverter.CaloricBreakdownConverter
import bangkit.xaplose.fudery.data.source.local.entityConverter.NutrientItemListConverter
import bangkit.xaplose.fudery.data.source.local.entityConverter.WeightPerServingConverter

@Database(entities = [FoodDetails::class], version = 1, exportSchema = false)
@TypeConverters(
    NutrientItemListConverter::class,
    WeightPerServingConverter::class,
    CaloricBreakdownConverter::class
)
abstract class FoodDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): FoodDatabase {
            if (INSTANCE == null) {
                synchronized(FoodDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FoodDatabase::class.java, "food_database")
                        .build()
                }
            }
            return INSTANCE as FoodDatabase
        }
    }

}