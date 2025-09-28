package com.example.auction.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.auction.R
import com.example.auction.data.viewmodels.JoinAuctionViewModel
import com.example.auction.databinding.ActivityJoinAuctionBinding

class JoinAuctionActivity : AppCompatActivity() {
    companion object {
        const val TAG = "Join_Auction"
    }

    private val joinAuctionViewModel: JoinAuctionViewModel by viewModels()
    private lateinit var binding: ActivityJoinAuctionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJoinAuctionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = intent.extras

        if (extras != null) {
            val auctionId = extras.getString("auctionId")
            val imageUri = extras.getString("imageUrl")
            val productName = extras.getString("productName")
            val productDescription = extras.getString("productDescription")
            val minBid = extras.getString("minBid")
            val maxBid = extras.getString("maxBid")
            val remainingTime = extras.getString("currentTime")

            if (remainingTime != "Auction Ended") {
                displayAuction(
                    auctionId,
                    imageUri,
                    productName,
                    productDescription,
                    minBid,
                    maxBid,
                    remainingTime
                )
            } else {
                displayAuction(
                    auctionId,
                    imageUri,
                    productName,
                    productDescription,
                    minBid,
                    maxBid,
                    "Auction Ended"
                )
            }
        } else {
            Log.e(TAG, "onCreate: JoinAuction: Data is null")
        }


    }

    @SuppressLint("SetTextI18n")
    private fun displayAuction(
        auctionId: String?,
        imageUri: String?,
        productName: String?,
        productDescription: String?,
        minBid: String?,
        maxBid: String?,
        remainingTime: String?
    ) {
        joinAuctionViewModel.getHighestBid(auctionId.toString()) {
            binding.productPriceJoin.text = it.toString()
        }
        Glide.with(this@JoinAuctionActivity).load(imageUri).error(R.drawable.img_error)
            .placeholder(R.drawable.img_placeholder).into(binding.productImgJoin)
        binding.productNameJoin.text = productName.toString()
        binding.productDesJoin.text =
            "${productDescription.toString()}\nMaximum Bid is: $maxBid\nMinimum Bid is :$minBid"
        binding.remainingTime.text = remainingTime.toString()

        if (remainingTime == "Auction Ended") {
            // Disable bidding if the auction has ended
            binding.highestBidText.text = "Price"
            if (auctionId != null) {
                joinAuctionViewModel.getHighestBid(auctionId) {
                    binding.productPriceJoin.text = it.toString()
                }
            }
            binding.placeBid.text = "Buy now"

//                Payment Method should be implemented
        } else {
            binding.placeBid.setOnClickListener {
                amountDialog(auctionId, maxBid)

            }
        }
    }

    private fun amountDialog(auctionId: String?, maxBid: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.bid_dialog)

        val amount = dialog.findViewById<EditText>(R.id.bid_amount)
        val bid = dialog.findViewById<Button>(R.id.btn_bid)

        bid.setOnClickListener {
            val bidAmount = amount.text.toString().toDoubleOrNull()

            if (bidAmount != null) {
                if (maxBid != null) {
                    joinAuctionViewModel.placeBid(
                        auctionId.toString(), bidAmount, maxBid = maxBid
                    ) {
                        if (it) {
                            Toast.makeText(this, "Bid Placed", Toast.LENGTH_SHORT).show()
                            binding.placeBid.text = "Bid Placed"
                        } else {
                            Toast.makeText(
                                this,
                                "Bid too high enough or too low",
                                Toast.LENGTH_SHORT
                            ).show()
                            amount.error = "Bid too high enough or too low"
                        }
                    }
                }
                dialog.dismiss()
            } else {
                amount.error = "Enter a valid amount"
            }
        }

        dialog.show()
    }
}
