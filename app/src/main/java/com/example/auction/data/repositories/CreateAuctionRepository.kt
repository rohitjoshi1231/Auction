package com.example.auction.data.repositories

import android.net.Uri
import com.example.auction.data.models.AuctionDetails
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CreateAuctionRepository {
    private val db = Firebase.firestore
    private val storageRef = FirebaseStorage.getInstance().reference

    fun createAuction(
        productName: String,
        productDescription: String,
        minBid: Double,
        maxBid: Double,
        imageUri: Uri,
        isAuctionFinished: Boolean,
        callback: (Boolean, String, String?) -> Unit
    ) {
        val fileName = "${UUID.randomUUID()}.jpeg"
        val imageRef = storageRef.child("Auction Images").child(fileName)

        imageRef.putFile(imageUri).addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val auctionId = UUID.randomUUID().toString()
                val auctionData = AuctionDetails(
                    auctionId,
                    productName,
                    productDescription,
                    minBid,
                    maxBid,
                    uri.toString(),
                    isAuctionFinished
                )
                val auctionRef = db.collection("Auction Details").document(auctionId)
                auctionRef.set(auctionData).addOnSuccessListener {
                    callback(true, "Auction created successfully", auctionId)
                }.addOnFailureListener {
                    callback(false, "Failed to create auction: ${it.message}", null)
                }
            }.addOnFailureListener {
                callback(false, "Failed to get download URL: ${it.message}", null)
            }
        }.addOnFailureListener {
            callback(false, "Failed to upload image: ${it.message}", null)
        }
    }


}
