package com.example.auction.ui.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.auction.R
import com.example.auction.data.DataHelper
import com.example.auction.data.viewmodels.CreateAuctionViewModel
import com.example.auction.databinding.FragmentCreateAuctionBinding
import com.example.auction.ui.activities.MainActivity
import java.util.Calendar

class CreateAuctionFragment : Fragment() {
    private lateinit var binding: FragmentCreateAuctionBinding
    private val viewModel: CreateAuctionViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var auctionId: String? = null
    private val changeImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data?.data
                if (data != null) {
                    selectedImageUri = data
                    binding.auctionImage.setImageURI(data)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.hideFab()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateAuctionBinding.inflate(inflater, container, false)

        binding.auctionImage.setOnClickListener {
            val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImage)
        }

        binding.btnAuctionSubmit.setOnClickListener {
            val productName = binding.auctionName.text.trim().toString()
            val productDescription = binding.auctionDescription.text.trim().toString()
            val minBid = binding.auctionMinBid.text.trim().toString()
            val maxBid = binding.auctionMaxBid.text.trim().toString()

            when {
                productName.isEmpty() -> binding.auctionName.error = "Auction Name is required"
                productDescription.isEmpty() -> binding.auctionDescription.error =
                    "Auction Description is required"

                minBid.isEmpty() -> binding.auctionMinBid.error = "Minimum price is required"
                maxBid.isEmpty() -> binding.auctionMaxBid.error = "Maximum price is required"
                selectedImageUri == null -> Toast.makeText(
                    requireContext(), "Auction image is required", Toast.LENGTH_SHORT
                ).show()

                else -> {
                    createAuction(
                        productName,
                        productDescription,
                        minBid.toDouble(),
                        maxBid.toDouble(),
                        selectedImageUri!!,
                    )
                    Toast.makeText(
                        requireContext(), "Submitting details please wait", Toast.LENGTH_SHORT
                    ).show()
                    resetForm()
                }
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAuction(
        productName: String,
        productDescription: String,
        minBid: Double,
        maxBid: Double,
        imageUri: Uri,
    ) {
        viewModel.createAuction(
            productName = productName,
            productDescription = productDescription,
            maxBid = maxBid,
            minBid = minBid,
            imageUri = imageUri,
            isAuctionFinished = false
        ) { isSuccess, message, auctionId ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (isSuccess) {
                Log.d("CreateAuctionFragment", "Auction created successfully with ID: $auctionId")
                this.auctionId = auctionId
                binding.btnAuctionSubmit.isEnabled = true
                if (auctionId != null) {
                    showDateTimePickerDialog(auctionId)
                    Toast.makeText(
                        requireContext(),
                        "Auction Created Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Log.e("CreateAuctionFragment", "Failed to create auction: $message")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDateTimePickerDialog(auctionId: String) {
        val calendar = Calendar.getInstance()

        // Set the start and end of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val monthStart = calendar.timeInMillis

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val monthEnd = calendar.timeInMillis

        // Set the calendar to the current date
        calendar.timeInMillis = System.currentTimeMillis()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    requireContext(), { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)

                        val selectedDateTime = calendar.time
                        DataHelper(requireContext()).setEndTime(auctionId, selectedDateTime)
                        binding.selectedDate.text = selectedDateTime.toString()
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
                )

                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = monthStart
        datePickerDialog.datePicker.maxDate = monthEnd

        datePickerDialog.show()
    }

    private fun resetForm() {
        binding.auctionImage.setBackgroundResource(R.drawable.img_20)
        binding.auctionName.text.clear()
        binding.auctionDescription.text.clear()
        binding.auctionMinBid.text.clear()
        binding.auctionMaxBid.text.clear()
        binding.auctionImage.setImageURI(null)
        selectedImageUri = null

        binding.btnAuctionSubmit.isEnabled = false
    }
}
