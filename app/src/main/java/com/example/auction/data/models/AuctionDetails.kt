package com.example.auction.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AuctionDetails(
    var auctionId: String = "",  // Add this field
    var productName: String = "",
    var productDescription: String = "",
    var minBid: Double = 0.0,
    var maxBid: Double = 0.0,
    var imageUri: String = "",
    var isAuctionFinished: Boolean = false,
) {
    constructor() : this("", "", "", 0.0, 0.0, "", false)
}
