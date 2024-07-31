package com.example.auction.data.repositories

import com.example.auction.data.models.AuctionBid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class JoinAuctionRepository {
    private val database = Firebase.database
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun placeBid(
        auctionId: String, currentBid: Double, maxBid: String?, callback: (Boolean) -> Unit
    ) {
        val myRef = database.getReference("AuctionBid").child(auctionId)
        myRef.get().addOnSuccessListener { snapshot ->
            val highestBid = snapshot.child("highestBid").getValue(Double::class.java) ?: 0.0
            val maxBidValue = maxBid?.toDouble()

            // Check if currentBid is within the allowable range
            if (currentBid > highestBid && (maxBidValue == null || currentBid <= maxBidValue)) {
                val biddingData =
                    AuctionBid(currentUser?.uid ?: "", auctionId, currentBid, currentBid, false)
                myRef.setValue(biddingData).addOnSuccessListener {
                    callback(true)
                }.addOnFailureListener {
                    callback(false)
                }
            } else {
                callback(false)
            }
        }.addOnFailureListener {
            callback(false)
        }
    }


    fun getHighestBid(auctionId: String, callback: (Double) -> Unit) {
        val myRef = database.getReference("AuctionBid").child(auctionId)
        myRef.get().addOnSuccessListener { snapshot ->
            val highestBid = snapshot.child("highestBid").getValue(Double::class.java) ?: 0.0
            callback(highestBid)
        }.addOnFailureListener {
            callback(0.0)
        }
    }
}
