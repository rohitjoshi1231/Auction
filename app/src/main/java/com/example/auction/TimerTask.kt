package com.example.auction

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.auction.data.DataHelper
import java.util.TimerTask

class TimeTask(
    private val auctionId: String,
    private val endTimeMillis: Long,
    private val context: Context,
    private val callback: (String, Boolean) -> Unit
) : TimerTask() {

    private val handler = Handler(Looper.getMainLooper())

    override fun run() {
        val remainingTime = endTimeMillis - System.currentTimeMillis()
        if (remainingTime <= 0) {
            cancel()
            handler.post {
                DataHelper(context).setAuctionFinished(auctionId, true)
                DataHelper(context).setWinner(auctionId)
                callback("00:00:00:00", true)
            }
        } else {
            handler.post {
                callback(timeStringFromLong(remainingTime), false)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60)) % 60
        val hours = (ms / (1000 * 60 * 60)) % 24
        val days = (ms / (1000 * 60 * 60 * 24))
        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
    }

}


