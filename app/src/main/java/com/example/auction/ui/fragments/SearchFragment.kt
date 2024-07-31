package com.example.auction.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auction.data.models.AuctionDetails
import com.example.auction.data.viewmodels.SearchViewModel
import com.example.auction.databinding.FragmentSearchBinding
import com.example.auction.ui.activities.MainActivity
import com.example.auction.ui.adapters.SearchAdapter

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var auctionList: MutableList<AuctionDetails>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.showFab()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)



        auctionList = mutableListOf()
        val searchRecycler = binding.searchRecycler

        searchAdapter = SearchAdapter(requireContext(), auctionList)
        searchRecycler.layoutManager = LinearLayoutManager(requireContext())
        searchRecycler.adapter = searchAdapter

        viewModel.finishedAuction { isAuctionFinished ->
            if (!isAuctionFinished) {
                binding.title.visibility = View.GONE
                binding.searchRecycler.visibility = View.GONE
                binding.noAuctionFrame.visibility = View.VISIBLE
            }
        }
        fetchAuction()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchAuction() {
        viewModel.auctionList.observe(viewLifecycleOwner) { auctions ->
            auctionList.clear()
            auctionList.addAll(auctions)
            searchAdapter.notifyDataSetChanged()
        }
    }
}
