package com.example.auction.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuctionViewModel : ViewModel() {
    private val _remainingTime = MutableLiveData<String>()
    val remainingTime: LiveData<String> = _remainingTime

    fun setRemainingTime(time: String) {
        _remainingTime.value = time
    }
}