package bangkit.xaplose.fudery.ui.fooddetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bangkit.xaplose.fudery.data.Repository
import bangkit.xaplose.fudery.data.model.FoodDetails
import kotlinx.coroutines.launch

class DetailsViewModel(private val mRepository: Repository) : ViewModel() {

    private val _foodDetails = MutableLiveData<FoodDetails>()

    fun getFoodDetailsById(id: Int): LiveData<FoodDetails> {
        viewModelScope.launch {
            _foodDetails.value = mRepository.getFoodById(id)
        }
        return _foodDetails
    }
}