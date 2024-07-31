package com.example.auction.data.repositories

import com.example.auction.data.models.AuctionDetails
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GetAuctionRepository {

    fun getAuction(callback: (List<AuctionDetails>) -> Unit) {
        val db = Firebase.firestore
        val auctionList = mutableListOf<AuctionDetails>()
        db.collection("Auction Details").get().addOnSuccessListener { result ->
            for (data in result) {
                val auctionData = data.toObject(AuctionDetails::class.java)
                if (!auctionData.isAuctionFinished) {
                    auctionList.add(auctionData)
                }
            }
            callback(auctionList)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}
