package bangkit.xaplose.fudery.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.databinding.FragmentHistoryBinding
import bangkit.xaplose.fudery.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.util.*

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HistoryFoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        historyViewModel =
            ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        setAdapter()
        historyViewModel.getAllFoodHistory().observe(viewLifecycleOwner) {
            updateData(it)
        }

        with(binding.rvHistoryList) {
            adapter = this@HistoryFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun updateData(data: List<FoodDetails>?) {
        if (data != null && data.isNotEmpty()) {
            adapter.setData(data)
            binding.tvNoHist.visibility = View.GONE
            binding.rvHistoryList.visibility = View.VISIBLE
            binding.scrollview.isFillViewport = false
        } else {
            binding.tvNoHist.visibility = View.VISIBLE
            binding.rvHistoryList.visibility = View.GONE
            binding.scrollview.isFillViewport = true
        }
    }

    private fun setAdapter() {
        adapter = HistoryFoodAdapter()
        adapter.onItemClick = {
            val action = HistoryFragmentDirections.actionNavigationHistoryToFoodDetailsFragment(
                id = it.id, history = true
            )
            findNavController().navigate(action)
        }
        adapter.onDeleteIconClick = {
            historyViewModel.deleteFoodHistory(it)
            Snackbar.make(
                requireView(),
                "${it.name.capitalize(Locale.ROOT)} is removed from search history.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}