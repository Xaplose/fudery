package bangkit.xaplose.fudery.di

import bangkit.xaplose.fudery.data.Repository
import bangkit.xaplose.fudery.data.source.remote.RemoteDataSource
import bangkit.xaplose.fudery.data.source.remote.network.RetrofitInstance

object Injection {
    fun provideRepository(): Repository {
        val dataSource = RemoteDataSource.getInstance(RetrofitInstance.api)
        return Repository.getInstance(dataSource)
    }
}