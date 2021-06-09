package bangkit.xaplose.fudery.ui.discover

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bangkit.xaplose.fudery.databinding.FragmentDiscoverBinding
import bangkit.xaplose.fudery.utils.Helper.getRealPathFromURI
import bangkit.xaplose.fudery.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class DiscoverFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: DiscoverViewModel
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private var action: Int = 0
    private var isPredictionStep = false
    private lateinit var foodPredictionName: String
    private lateinit var foodAdapter: DiscoverFoodAdapter

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            checkAndRequestPermission()
        }
    }

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
        observeFoodPrediction()
    }

    private fun observeGetFoodListByName() {
        viewModel.foodList.observe(viewLifecycleOwner, { foodList ->
            showLoading(false)
            if (isPredictionStep) {
                isPredictionStep = false
                if (foodList.isEmpty()) {
                    Snackbar.make(
                        requireView(),
                        "Can't find nutrition of $foodPredictionName",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    moveToDetailsPage(foodList[0].id)
                }
            } else {
                foodAdapter.setData(foodList)
            }
        })
    }

    private fun observeFoodPrediction() {
        viewModel.foodPrediction.observe(viewLifecycleOwner, { foodPrediction ->
            showLoading(true)
            foodPredictionName = foodPrediction.name
            viewModel.getFoodListByName(foodPrediction.name)
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
            moveToDetailsPage(selectedFood.id)
        }
    }

    private fun moveToDetailsPage(id: Int) {
        val action =
            DiscoverFragmentDirections.actionNavigationDiscoverToFoodDetailsFragment(id = id)
        findNavController().navigate(action)
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
                btnCamera.id -> {
                    action = REQUEST_IMAGE_CAPTURE
                    checkAndRequestPermission()
                }
                btnGallery.id -> {
                    action = REQUEST_IMAGE_GALLERY
                    checkAndRequestPermission()
                }
            }
        }
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            imageAction()
        }
    }

    private fun imageAction() {
        if (action == REQUEST_IMAGE_CAPTURE) {
            takePictureIntent()
        } else if (action == REQUEST_IMAGE_GALLERY) {
            openGalleryIntent()
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
            saveImage(imageBitmap, requireContext())
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            isPredictionStep = true
            val imageUri = data?.data
            viewModel.predict(getRealPathFromURI(requireContext(), imageUri!!)!!)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun saveImage(bitmap: Bitmap, mContext: Context) {
        val fileName: String

        ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            fileName = UUID.nameUUIDFromBytes(this.toByteArray()).toString().replace("-", "")
        }

        val filePath =
            "${mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/$fileName.jpg/"
        val imageFile = File(filePath)

        if (!imageFile.exists()) {
            val contentResolver = ContentValues().apply {
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            }
            mContext.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentResolver
            ).apply {
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    mContext.contentResolver.openOutputStream(this!!)
                )
            }
            isPredictionStep = true
            viewModel.predict(filePath)
        } else
            Snackbar.make(
                requireView(),
                "Something happened, please try again.",
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}