package com.example.auction.data.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {


    val isAuctionFinished = MutableLiveData<Boolean>()

    fun auction(auctionFinish: Boolean) {
        isAuctionFinished.value = auctionFinish
    }
}
