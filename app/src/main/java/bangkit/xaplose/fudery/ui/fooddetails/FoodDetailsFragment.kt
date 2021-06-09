package bangkit.xaplose.fudery.ui.fooddetails

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.xaplose.fudery.R
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.databinding.FragmentFoodDetailsBinding
import bangkit.xaplose.fudery.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import java.util.*

private const val FOOD_ID = "id"
private const val FOOD_HIST = "history"

class FoodDetailsFragment : Fragment() {

    private var id: Int? = null
    private var isHistoryPage = false
    private var isDeleted = false
    private lateinit var binding: FragmentFoodDetailsBinding
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var adapter: FoodNutrientAdapter
    private lateinit var deleteMenuItem: MenuItem
    private var foodDetails: FoodDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(FOOD_ID)
            isHistoryPage = it.getBoolean(FOOD_HIST)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (isHistoryPage) {
            inflater.inflate(R.menu.history_menu, menu)
            deleteMenuItem = menu.findItem(R.id.action_delete)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete && !isDeleted) {
            detailsViewModel.deleteFromHistory(foodDetails as FoodDetails)
            Snackbar.make(
                requireView(),
                "${foodDetails?.name?.capitalize(Locale.ROOT)} is removed from search history.",
                Snackbar.LENGTH_SHORT
            ).show()
            deleteMenuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_deleted)
            isDeleted = true
        }
        return super.onOptionsItemSelected(item)
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
        val factory = ViewModelFactory.getInstance(requireContext())
        detailsViewModel = ViewModelProvider(this, factory)[DetailsViewModel::class.java]

        adapter = FoodNutrientAdapter(requireContext())
        with(binding.rvNutrients) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FoodDetailsFragment.adapter
            setHasFixedSize(true)
        }

        setUpObserver()
    }

    private fun setUpObserver() {
        if (isHistoryPage) {
            detailsViewModel.getFoodHistoryById(id as Int).observe(viewLifecycleOwner) {
                foodDetails = it
                updateUI(it)
            }
        } else {
            detailsViewModel.getFoodDetailsById(id as Int).observe(viewLifecycleOwner) {
                foodDetails = it
                updateUI(it)
                if (it.id != -1) {
                    detailsViewModel.addToHistory(it)
                }
            }
        }
    }

    private fun updateUI(foodDetails: FoodDetails?) {
        if (foodDetails != null) {
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
                    tvProteinPercentage.text =
                        getString(R.string.desc_percentage, this.percentProtein)
                }
                adapter.setData(foodDetails.nutrients)
            }
            showLoading(false)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}