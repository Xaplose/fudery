package bangkit.xaplose.fudery.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bangkit.xaplose.fudery.data.Repository
import bangkit.xaplose.fudery.data.model.Food
import bangkit.xaplose.fudery.data.model.FoodPrediction
import kotlinx.coroutines.launch

class DiscoverViewModel(private val mRepository: Repository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is discover Fragment"
    }
    val text: LiveData<String> = _text

    val foodList = MutableLiveData<List<Food>>()
    val foodPrediction = MutableLiveData<FoodPrediction>()

    fun getFoodListByName(name: String) {
        viewModelScope.launch {
            val foodListRes = mRepository.getFoodListByName(name)
            foodList.value = foodListRes
        }
    }

    fun predict(imgFilePath: String) {
        viewModelScope.launch {
            val foodPredictionRes = mRepository.predict(imgFilePath)
            foodPredictionRes.name = foodPredictionRes.name.replace("_", " ")
            foodPrediction.value = foodPredictionRes
        }
    }
}