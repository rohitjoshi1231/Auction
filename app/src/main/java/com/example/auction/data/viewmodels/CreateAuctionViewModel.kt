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
        endTime: Long?,

        callback: (Boolean, String, String?) -> Unit
    ) {
        repository.createAuction(
            productName = productName,
            productDescription = productDescription,
            minBid = minBid,
            maxBid = maxBid,
            imageUri = imageUri,
            isAuctionFinished = isAuctionFinished,
            endTime = endTime,
            callback = callback

        )
    }

}
