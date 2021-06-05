package bangkit.xaplose.fudery.ui.fooddetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.xaplose.fudery.R
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.databinding.FragmentFoodDetailsBinding
import bangkit.xaplose.fudery.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

private const val FOOD_ID = "id"

class FoodDetailsFragment : Fragment() {

    private var id: Int? = null
    private lateinit var binding: FragmentFoodDetailsBinding
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var adapter: FoodNutrientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(FOOD_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        val factory = ViewModelFactory.getInstance()
        detailsViewModel = ViewModelProvider(this, factory)[DetailsViewModel::class.java]

        adapter = FoodNutrientAdapter(requireContext())
        with(binding.rvNutrients) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FoodDetailsFragment.adapter
            setHasFixedSize(true)
        }

        detailsViewModel.getFoodDetailsById(id as Int).observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(foodDetails: FoodDetails) {
        with(binding) {
            Glide.with(requireContext())
                .load(foodDetails.imageUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_item_loading)
                        .error(R.drawable.ic_error)
                )
                .into(imgFood)

            tvFoodName.text = foodDetails.name.capitalize(Locale.ROOT)
            with(foodDetails.weightPerServing) {
                tvWps.text = getString(R.string.desc_weight_per_serving, this.amount, this.unit)
            }
            with(foodDetails.caloricBreakdown) {
                tvCarbsPercentage.text = getString(R.string.desc_percentage, this.percentCarbs)
                tvFatPercentage.text = getString(R.string.desc_percentage, this.percentFat)
                tvProteinPercentage.text = getString(R.string.desc_percentage, this.percentProtein)
            }
            adapter.setData(foodDetails.nutrients)
        }
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            FoodDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(FOOD_ID, id)
                }
            }
    }
}