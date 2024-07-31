package com.example.auction.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.auction.data.models.AuctionDetails
import com.example.auction.data.repositories.SearchAuctionRepository

class SearchViewModel : ViewModel() {
    private val searchRepository = SearchAuctionRepository()
    private val _auctionList = MutableLiveData<List<AuctionDetails>>()
    val auctionList: LiveData<List<AuctionDetails>> get() = _auctionList

    fun finishedAuction(callback: (Boolean) -> Unit) {
        searchRepository.finishedAuction { auctions, isAuctionFinished ->
            _auctionList.value = auctions
            callback(isAuctionFinished)
        }
    }
}
