package com.example.auction.data.models

data class AuctionBid(
    var userId: String = "",
    var auctionId: String = "",
    var currentBid: Double = 0.0,
    var highestBid: Double = 0.0,
    var isWinner: Boolean = false
)
