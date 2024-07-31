package com.example.auction.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.auction.R
import com.example.auction.data.viewmodels.UserProfileViewModel
import com.example.auction.ui.activities.MainActivity

class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UserProfileFragment()
    }

    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.showFab()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }
}