package com.example.auction.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class DataHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    private val timerCountingMap = mutableMapOf<String, Boolean>()
    private val endTimeMap =
        mutableMapOf<String, Long>() // Change to Long for storing time in millis

    init {
        // Load timer states and end times from SharedPreferences
        val allKeys = sharedPreferences.all.keys
        for (key in allKeys) {
            if (key.startsWith(END_TIME_PREFIX)) {
                val auctionId = key.removePrefix(END_TIME_PREFIX)
                val endString = sharedPreferences.getString(key, null)
                if (endString != null) {
                    endTimeMap[auctionId] = endString.toLongOrNull() ?: continue
                }
            } else if (key.startsWith(COUNTING_TIME_PREFIX)) {
                val auctionId = key.removePrefix(COUNTING_TIME_PREFIX)
                timerCountingMap[auctionId] = sharedPreferences.getBoolean(key, false)
            }
        }
    }

    fun setWinner(auctionId: String) {
        val dbRef = FirebaseDatabase.getInstance()
            .getReference("AuctionBid")
            .child(auctionId)

        dbRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val highestUserId = snapshot.child("userId").getValue(String::class.java)
                if (highestUserId != null) {
                    dbRef.child("winner").setValue(true)
                        .addOnSuccessListener {
                            Log.d("Auction", "Winner set true for $highestUserId")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Auction", "Failed to set winner: ${e.message}")
                        }
                }
            }
        }
    }


    fun endTime(auctionId: String): Date? {
        val endTimeMillis = endTimeMap[auctionId] ?: return null
        println("auctionid: $auctionId")
        println("endTimeMap: $endTimeMap")
        println("emd; ${endTimeMillis}")
        return Date(endTimeMillis)
    }


    fun setEndTime(auctionId: String, date: Date?) {
        endTimeMap[auctionId] = date?.time ?: 0L // Store time in millis
        println("time; ${date?.time ?: 0}")
        with(sharedPreferences.edit()) {
            val stringData = date?.time?.toString()
            putString("$END_TIME_PREFIX$auctionId", stringData)
            apply()
        }
    }


    fun setAuctionFinished(auctionId: String, isFinished: Boolean) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Auction Details").document(auctionId)
            .update("auctionFinished", isFinished)
    }


    companion object {
        const val PREFERENCES = "prefs"
        private const val END_TIME_PREFIX = "endKey_"
        private const val COUNTING_TIME_PREFIX = "countingKey_"
    }
}
