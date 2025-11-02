    package com.example.auction.data.viewmodels

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import com.example.auction.data.models.AuctionDetails
    import com.example.auction.data.repositories.GetAuctionRepository

    class HomeViewModel : ViewModel() {
        private val auctionRepository = GetAuctionRepository()
        private val _auctionList = MutableLiveData<List<AuctionDetails>>()
        val auctionList: LiveData<List<AuctionDetails>> get() = _auctionList

        fun getUserWonAuctions(callback: (List<String>) -> Unit) {
            auctionRepository.checkAuctionWinners { wonList ->
                callback(wonList)
            }
        }

        fun fetchAuctionDetails(auctionId: String, callback: (AuctionDetails?) -> Unit) {
            auctionRepository.getAuctionDetailsById(auctionId) { auction ->
                callback(auction)
            }
        }


        fun getAuction(callback: (Boolean) -> Unit) {
            auctionRepository.getAuction { auctions ->
                _auctionList.value = auctions
                callback(auctions.isEmpty())
            }
        }
    }
