package bangkit.xaplose.fudery.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bangkit.xaplose.fudery.SignInActivity
import bangkit.xaplose.fudery.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val user = Firebase.auth.currentUser as FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        setSignOutButtonListener()
        bindUserInfo()
    }

    private fun bindUserInfo() {
        with(binding) {
            tvEmail.text = user.email
            tvUid.text = user.uid
            tvUname.text = user.displayName

            Glide.with(requireContext())
                .load(user.photoUrl)
                .into(imgUser)
        }
    }

    private fun setSignOutButtonListener() {
        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()
            goToSignInActivity()
        }
    }

    private fun goToSignInActivity() {
        val intent = Intent(requireActivity(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}