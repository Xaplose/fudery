package bangkit.xaplose.fudery.ui.discover

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.xaplose.fudery.databinding.FragmentDiscoverBinding
import bangkit.xaplose.fudery.viewmodel.ViewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DiscoverFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: DiscoverViewModel
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodAdapter: DiscoverFoodAdapter

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUsername.text = Firebase.auth.currentUser?.displayName as String
        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)

        setupViewModel()
        setupSearchView()
        setupRecyclerViewAdapter()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DiscoverViewModel::class.java]
        viewModel.getFoodListByName("Apple")
        observeGetFoodListByName()
    }

    private fun observeGetFoodListByName() {
        viewModel.foodList.observe(viewLifecycleOwner, { foodList ->
            showLoading(false)
            foodAdapter.setData(foodList)
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showLoading(true)
                if (query.isNullOrBlank()) {
                    viewModel.getFoodListByName("Apple")
                } else {
                    viewModel.getFoodListByName(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrBlank()) {
                    showLoading(true)
                    viewModel.getFoodListByName("Apple")
                }
                return false
            }
        })
    }

    private fun setupRecyclerViewAdapter() {
        foodAdapter = DiscoverFoodAdapter()
        foodAdapter.onItemClick = { selectedFood ->
            val action = DiscoverFragmentDirections.actionNavigationDiscoverToFoodDetailsFragment(id=selectedFood.id)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvFoodList) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = foodAdapter
        }
    }

    override fun onClick(view: View?) {
        binding.apply {
            when (view?.id) {
                btnCamera.id -> takePictureIntent()
                btnGallery.id -> openGalleryIntent()
            }
        }
    }

    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun openGalleryIntent() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(gallery, REQUEST_IMAGE_GALLERY)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgPhoto.setImageBitmap(imageBitmap)
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            val imageUri = data?.data
            binding.imgPhoto.setImageURI(imageUri)
            Log.d("IMAGE_URI: ", imageUri.toString())
            viewModel.predict(imageUri.toString())
        }
        binding.imgPhoto.visibility = View.VISIBLE
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}