package com.example.auction.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.auction.R
import com.example.auction.data.viewmodels.HomeViewModel
import com.example.auction.data.viewmodels.SharedViewModel
import com.example.auction.databinding.FragmentHomeBinding
import com.example.auction.ui.activities.MainActivity
import com.example.auction.ui.adapters.AuctionAdapter
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var auctionAdapter: AuctionAdapter

    private lateinit var model: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        (activity as? MainActivity)?.showFab()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        auctionAdapter = AuctionAdapter(model, requireContext(), mutableListOf())
        // Set up RecyclerView
        binding.homeRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = auctionAdapter
        }

        // Set user greeting and profile image
        binding.userName.text = if (auth.currentUser?.displayName?.isEmpty() == true) {
            "Hello Guest"
        } else {
            "Hello ${auth.currentUser?.displayName}"
        }

        Glide.with(requireContext()).load(auth.currentUser?.photoUrl)
            .error(R.drawable.user_not_found).into(binding.homeUserProfile)

        homeViewModel.getAuction {

        }
        observeAuctionData()
    }

    private fun observeAuctionData() {
        homeViewModel.auctionList.observe(viewLifecycleOwner) { auctions ->
            if (auctions.isEmpty()) {
                binding.noAuctionFrame.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            } else {
                binding.noAuctionFrame.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
                auctionAdapter.updateData(auctions)
            }
        }
    }
}
