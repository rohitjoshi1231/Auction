package com.example.auction.data.viewmodels

import androidx.lifecycle.ViewModel
import com.example.auction.data.repositories.JoinAuctionRepository

class JoinAuctionViewModel : ViewModel() {
    private val joinAuctionRepository = JoinAuctionRepository()

    fun placeBid(
        auctionId: String,
        currentBid: Double,
        maxBid: String?,
        callback: (Boolean) -> Unit
    ) {
        joinAuctionRepository.placeBid(auctionId, currentBid, maxBid, callback)
    }

    fun getHighestBid(auctionId: String, callback: (Double) -> Unit) {
        joinAuctionRepository.getHighestBid(auctionId, callback)
    }
}
