package com.example.auction.data.repositories

import com.example.auction.data.models.AuctionDetails
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

class SearchAuctionRepository {
    private val db = Firebase.firestore

    fun finishedAuction(callback: (List<AuctionDetails>, Boolean) -> Unit) {
        val auctionList = mutableListOf<AuctionDetails>()
        db.collection("Auction Details").get().addOnSuccessListener { result ->
            for (data in result) {
                val auctionData = data.toObject(AuctionDetails::class.java)
                if (auctionData.isAuctionFinished) {
                    auctionList.add(auctionData)
                }
            }
            callback(auctionList, auctionList.isNotEmpty())
        }.addOnFailureListener {
            callback(emptyList(), false)
        }
    }
}
