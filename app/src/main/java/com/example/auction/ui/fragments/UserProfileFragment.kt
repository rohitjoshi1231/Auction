package com.example.auction.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.auction.data.viewmodels.UserProfileViewModel
import com.example.auction.databinding.FragmentUserProfileBinding
import com.example.auction.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth

class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UserProfileFragment()
    }

    private lateinit var auth: FirebaseAuth


    private val viewModel: UserProfileViewModel by viewModels()
    lateinit var binding: FragmentUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.showFab()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        binding.emailText.text = auth.currentUser?.email ?: "example@gmail.com"
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            activity?.finish()
        }
        return binding.root
    }
}