package bangkit.xaplose.fudery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bangkit.xaplose.fudery.data.Repository
import bangkit.xaplose.fudery.di.Injection
import bangkit.xaplose.fudery.ui.discover.DiscoverViewModel

class ViewModelFactory private constructor(private val mRepository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository()).apply {
                    instance = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DiscoverViewModel::class.java) -> {
                DiscoverViewModel(mRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}