package com.example.auction.data.repositories

import android.util.Log
import com.example.auction.data.models.AuctionDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class GetAuctionRepository {


    fun checkAuctionWinners(callback: (List<String>) -> Unit) {
        val dbRef = FirebaseDatabase.getInstance().getReference("AuctionBid")
        val auth = FirebaseAuth.getInstance()

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val wonAuctions = mutableListOf<String>()

                if (snapshot.exists()) {
                    for (auctionSnap in snapshot.children) {
                        val winner =
                            auctionSnap.child("winner").getValue(Boolean::class.java) ?: false
                        if (winner) {
                            val auctionId =
                                auctionSnap.child("auctionId").getValue(String::class.java)
                                    ?: "Unknown"
                            val userId = auctionSnap.child("userId").getValue(String::class.java)
                                ?: "Unknown"

                            if (auth.currentUser?.uid == userId) {
                                wonAuctions.add(auctionId)
                            }
                        }
                    }
                }

                callback(wonAuctions)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }



    fun getAuctionDetailsById(auctionId: String, callback: (AuctionDetails?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("Auction Details").document(auctionId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val auction = document.toObject(AuctionDetails::class.java)
                    callback(auction)
                } else {
                    Log.d("Auction", "No auction found with ID: $auctionId")
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Auction", "Error fetching auction: ${e.message}")
                callback(null)
            }
    }



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

                            val timerSnapshot = snapshot.child(auctionData.auctionId)
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
