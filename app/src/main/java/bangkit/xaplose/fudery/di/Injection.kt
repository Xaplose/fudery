package bangkit.xaplose.fudery.di

import android.content.Context
import bangkit.xaplose.fudery.data.Repository
import bangkit.xaplose.fudery.data.source.local.room.FoodDatabase
import bangkit.xaplose.fudery.data.source.remote.RemoteDataSource
import bangkit.xaplose.fudery.data.source.remote.network.RetrofitInstance

object Injection {
    fun provideRepository(context: Context): Repository {
        val dataSource = RemoteDataSource.getInstance(RetrofitInstance.api)
        val database = FoodDatabase.getDatabase(context)
        return Repository.getInstance(dataSource, database.foodDao())
    }
}