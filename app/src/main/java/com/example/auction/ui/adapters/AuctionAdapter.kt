package com.example.auction.ui.adapters

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.auction.R
import com.example.auction.TimeTask
import com.example.auction.data.DataHelper
import com.example.auction.data.models.AuctionDetails
import com.example.auction.data.viewmodels.SharedViewModel
import com.example.auction.ui.activities.JoinAuctionActivity
import java.util.Timer

class AuctionAdapter(
    private val sharedViewModel: SharedViewModel,
    private val context: Context,
    private val auctionList: MutableList<AuctionDetails>
) : RecyclerView.Adapter<AuctionAdapter.GridViewHolder>() {

    private val timerTasks = mutableMapOf<String, TimeTask>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAuctionList: List<AuctionDetails>) {
        auctionList.clear()
        auctionList.addAll(newAuctionList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.auction_detailss_list, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val currentItem = auctionList[position]

        Log.d("AuctionAdapter", "Auction ID: ${currentItem.auctionId}")

        startOrRefreshTimer(currentItem.auctionId, holder) { remainingTime ->
            val options =
                ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out)

            holder.btnJoinAuction.setOnClickListener {
                showIntent(currentItem, remainingTime, options)
            }
            holder.itemView.setOnClickListener {
                showIntent(currentItem, remainingTime, options)
            }
        }

        holder.productName.text = currentItem.productName
        holder.productPrice.text = currentItem.maxBid.toString()

        Glide.with(context)
            .load(Uri.parse(currentItem.imageUri))
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.imageView)
    }


    private fun showIntent(
        currentItem: AuctionDetails,
        remainingTime: String,
        options: ActivityOptions
    ) {
        val intent = Intent(context, JoinAuctionActivity::class.java).apply {
            putExtra("auctionId", currentItem.auctionId)
            putExtra("imageUrl", currentItem.imageUri)
            putExtra("productName", currentItem.productName)
            putExtra("productDescription", currentItem.productDescription)
            putExtra("maxBid", currentItem.maxBid.toString())
            putExtra("minBid", currentItem.minBid.toString())
            putExtra("currentTime", remainingTime)
        }
        context.startActivity(intent, options.toBundle())
    }

    private fun startOrRefreshTimer(
        auctionId: String, holder: GridViewHolder, callback: (String) -> Unit
    ) {
        val endTime = DataHelper(context).endTime(auctionId)
        endTime?.let {
            timerTasks[auctionId]?.cancel()

            val timerTask =
                TimeTask(auctionId, it.time, context) { remainingTime, isAuctionFinished ->
                    sharedViewModel.auction(isAuctionFinished)
                    holder.auctionTime.text = remainingTime
                    callback(remainingTime)
                }
            timerTasks[auctionId] = timerTask
            Timer().schedule(timerTask, 0, 1000)
        }
    }

    override fun onViewRecycled(holder: GridViewHolder) {
        super.onViewRecycled(holder)
        auctionList.getOrNull(holder.adapterPosition)?.auctionId?.let { auctionId ->
            timerTasks[auctionId]?.cancel()
        }
    }

    override fun getItemCount(): Int {
        return auctionList.size
    }

    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val auctionTime: TextView = itemView.findViewById(R.id.product_offer)
        val btnJoinAuction: Button = itemView.findViewById(R.id.btn_join_auction)
    }
}
