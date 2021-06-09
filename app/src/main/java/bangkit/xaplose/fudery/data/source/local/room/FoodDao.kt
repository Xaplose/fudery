package bangkit.xaplose.fudery.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import bangkit.xaplose.fudery.data.model.FoodDetails

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(food: FoodDetails)

    @Delete
    fun delete(food: FoodDetails)

    @Query("SELECT * from foodentity")
    fun getAllFood(): LiveData<List<FoodDetails>>

    @Query("SELECT * FROM foodentity WHERE id = :foodId")
    fun getFoodById(foodId: Int): LiveData<FoodDetails>
}