package com.example.auction.ui.adapters

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.auction.R
import com.example.auction.data.models.AuctionDetails
import com.example.auction.ui.activities.JoinAuctionActivity

class SearchAdapter(private val context: Context, private val auctionList: List<AuctionDetails>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_auction_details, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val currentItem = auctionList[position]

        val options = ActivityOptions.makeCustomAnimation(context, R.anim.fade_in, R.anim.fade_out)

        holder.productName.text = currentItem.productName
        holder.productPrice.text = currentItem.maxBid.toString()
        Glide.with(context).load(currentItem.imageUri).error(R.drawable.img_error)
            .placeholder(R.drawable.img_placeholder).into(holder.imageView)

        holder.itemView.setOnClickListener {

            val intent = Intent(context, JoinAuctionActivity::class.java).apply {
                putExtra("auctionId", currentItem.auctionId)
                putExtra("imageUrl", currentItem.imageUri)
                putExtra("productName", currentItem.productName)
                putExtra("productDescription", currentItem.productDescription)
                putExtra("maxBid", currentItem.maxBid.toString())
                putExtra("minBid", currentItem.minBid.toString())
                putExtra("currentTime", "Auction Ended")
            }
            context.startActivity(intent, options.toBundle())
        }

    }


    override fun getItemCount(): Int = auctionList.size


    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.product_image_search)
        val productName: TextView = itemView.findViewById(R.id.product_name_search)
        val productPrice: TextView = itemView.findViewById(R.id.product_price_search)
    }
}
