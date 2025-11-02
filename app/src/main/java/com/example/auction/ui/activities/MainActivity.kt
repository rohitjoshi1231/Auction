package com.example.auction.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.auction.R
import com.example.auction.databinding.ActivityMainBinding
import com.example.auction.ui.fragments.CreateAuctionFragment
import com.example.auction.ui.fragments.HomeFragment
import com.example.auction.ui.fragments.NotificationFragment
import com.example.auction.ui.fragments.SearchFragment
import com.example.auction.ui.fragments.UserProfileFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var fab: FloatingActionButton

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fab = binding.fab
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        binding.bottomNavigationView.background = null

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        openDashboard()
        binding.fab.setOnClickListener {
            loadFragment(CreateAuctionFragment(), "Create Auction")
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.navigation_home -> openDashboard()
                R.id.navigation_search -> loadFragment(SearchFragment(), "Discover")
                R.id.navigation_notification ->
//                    Toast.makeText(
//                    this,
//                    "Feature needs to be implemented",
//                    Toast.LENGTH_SHORT
//                ).show()
                    loadFragment(
                        NotificationFragment(), "Notification"
                )

                R.id.navigation_user_profile ->
//                Toast.makeText(
//                    this,
//                    "Feature needs to be implemented",
//                    Toast.LENGTH_SHORT
//                ).show()
                    loadFragment(
                        UserProfileFragment(), "Account"
                )
            }
            true
        }
    }

    fun hideFab() {
        fab.hide()
    }

    fun showFab() {
        fab.show()
    }

    private fun openDashboard() {
        loadFragment(HomeFragment(), "Auction")
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        setUpToolbar(title)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack(title).commit()
    }

    private fun setUpToolbar(title: String) {
        supportActionBar?.title = title
    }
}