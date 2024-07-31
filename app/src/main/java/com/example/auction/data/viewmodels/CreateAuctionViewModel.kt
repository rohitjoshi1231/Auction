package com.example.auction.data.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.auction.data.repositories.CreateAuctionRepository

class CreateAuctionViewModel : ViewModel() {
    private val repository = CreateAuctionRepository()

    fun createAuction(
        productName: String,
        productDescription: String,
        minBid: Double,
        maxBid: Double,
        imageUri: Uri,
        isAuctionFinished: Boolean,
        callback: (Boolean, String, String?) -> Unit
    ) {
        repository.createAuction(
            productName,
            productDescription,
            minBid,
            maxBid,
            imageUri,
            isAuctionFinished,
            callback
        )
    }

}
