package com.example.auction.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.auction.R
import com.example.auction.data.models.UserCredential
import com.example.auction.data.viewmodels.EmailRegisterViewModel
import com.example.auction.databinding.ActivityEmailRegisterBinding

class EmailRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailRegisterBinding
    private val emailRegisterViewModel: EmailRegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEmailRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnEmailRegister.setOnClickListener {
            val email = binding.edTxtEmailRegister.text.toString().trim()
            val password = binding.edTxtPasswordRegister.text.toString().trim()
            val confirmPassword = binding.edTxtConfirmPasswordRegister.text.toString().trim()
            val termsAndPolicy = binding.termsAndPolicy.isChecked

            when {
                email.isEmpty() -> {
                    Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
                }

                password.isEmpty() -> {
                    Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                }

                confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
                }

                !termsAndPolicy -> {
                    Toast.makeText(
                        this,
                        "You must agree to the terms and policy",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                password != confirmPassword -> {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    registerWithEmail(email = email, password = password)
                    // Clear all fields
                    binding.edTxtEmailRegister.text.clear()
                    binding.edTxtPasswordRegister.text.clear()
                    binding.edTxtConfirmPasswordRegister.text.clear()
                    binding.termsAndPolicy.isChecked = false

                    // Disable the button
                    binding.btnEmailRegister.isEnabled = false
                }
            }
        }

    }

    private fun registerWithEmail(email: String, password: String) {

        val userCredential = UserCredential(email = email)
        emailRegisterViewModel.registerWithEmail(userCredential, password, { isSuccess, message ->
            if (isSuccess) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@EmailRegisterActivity, VerifyEmailActivity::class.java))
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }, { emailVerified ->
            if (emailVerified) {
                startActivity(Intent(this@EmailRegisterActivity, MainActivity::class.java))
            }
        })

    }
}