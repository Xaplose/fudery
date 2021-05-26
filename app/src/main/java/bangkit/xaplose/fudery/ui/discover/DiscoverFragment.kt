package bangkit.xaplose.fudery.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bangkit.xaplose.fudery.databinding.FragmentDiscoverBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DiscoverFragment : Fragment() {

    private lateinit var discoverViewModel: DiscoverViewModel
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        discoverViewModel =
            ViewModelProvider(this).get(DiscoverViewModel::class.java)

        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvUsername.text = Firebase.auth.currentUser?.displayName as String
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}