package bangkit.xaplose.fudery.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bangkit.xaplose.fudery.data.Repository
import bangkit.xaplose.fudery.data.model.FoodDetails

class HistoryViewModel(
    private val mRepository: Repository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text

    fun getAllFoodHistory() = mRepository.getAllFoodHistory()

    fun getFoodHistoryById(id: Int) = mRepository.getFoodHistoryById(id)

    fun deleteFoodHistory(food: FoodDetails) = mRepository.delete(food)
}