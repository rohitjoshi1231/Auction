package com.example.auction.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.auction.R
import com.example.auction.data.viewmodels.HomeViewModel
import com.example.auction.databinding.FragmentNotificationBinding
import com.example.auction.ui.activities.MainActivity

class NotificationFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.showFab()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        viewModel.getUserWonAuctions { wonList ->
            if (wonList.isNotEmpty()) {
                binding.congoText.visibility = View.VISIBLE
                binding.congoText.text = "Congratulations you have won these auctions"
                wonList.forEach { auctionId ->
                    viewModel.fetchAuctionDetails(auctionId) { auction ->
                        if (auction != null && isAdded) {  // check fragment still active
                            val inflater = LayoutInflater.from(requireContext())
                            val itemView = inflater.inflate(
                                R.layout.item_won_auction,
                                binding.auctionContainer,
                                false
                            )

                            val productName = itemView.findViewById<TextView>(R.id.itemProductName)
                            val productDesc =
                                itemView.findViewById<TextView>(R.id.itemProductDescription)
                            val bidAmount = itemView.findViewById<TextView>(R.id.itemBidAmount)
                            val productImage =
                                itemView.findViewById<ImageView>(R.id.itemProductImage)

                            productName.text = auction.productName
                            productDesc.text = auction.productDescription
                            bidAmount.text = "₹${auction.maxBid ?: 0}"

                            Glide.with(requireContext()).load(auction.imageUri).into(productImage)

                            // Add the view into the container
                            binding.auctionContainer.addView(itemView)
                        }
                    }
                }
            } else {
                binding.noWonImg.visibility = View.VISIBLE
                binding.noText.visibility = View.VISIBLE
            }
        }


        return binding.root
    }
}