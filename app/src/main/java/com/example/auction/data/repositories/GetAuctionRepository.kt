package com.example.auction.data.repositories

import com.example.auction.data.models.AuctionDetails
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

class GetAuctionRepository {

    fun getAuction(callback: (List<AuctionDetails>) -> Unit) {
        val db = Firebase.firestore
        val realtimeDb = Firebase.database
        val auctionList = mutableListOf<AuctionDetails>()

        db.collection("Auction Details").get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    callback(emptyList())
                    return@addOnSuccessListener
                }

                val auctionTimerRef = realtimeDb.getReference("auctionTimer")
                auctionTimerRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        auctionList.clear()

                        for (doc in result) {
                            val auctionData = doc.toObject(AuctionDetails::class.java)

                            val timerSnapshot = snapshot.child(auctionData.auctionId ?: "")
                            val endTime = timerSnapshot.child("endTime").getValue(Long::class.java)

                            if (endTime != null) {
                                auctionData.endTime = endTime
                            }

                            if (!auctionData.isAuctionFinished) {
                                auctionList.add(auctionData)
                            }
                        }

                        callback(auctionList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback(emptyList())
                    }
                })
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }
}
