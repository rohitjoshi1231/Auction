package com.example.auction.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.auction.data.models.UserCredential
import com.example.auction.data.viewmodels.EmailRegisterViewModel
import com.example.auction.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val emailRegisterViewModel: EmailRegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.newRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, EmailRegisterActivity::class.java))

        }
        binding.btnEmailLogin.setOnClickListener {
            val email = binding.edTxtEmailLogin.text.trim()
            val password = binding.edTxtPasswordLogin.text.trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email = email.toString(), password = password.toString())
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun login(email: String, password: String) {

        val userCredential = UserCredential(email = email)
        emailRegisterViewModel.loginWithEmail(userCredential, password) { isSuccess, message ->
            if (isSuccess) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}