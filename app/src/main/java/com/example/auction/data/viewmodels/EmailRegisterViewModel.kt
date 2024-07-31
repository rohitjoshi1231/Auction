package com.example.auction.data.viewmodels

import androidx.lifecycle.ViewModel
import com.example.auction.data.models.UserCredential
import com.example.auction.data.repositories.RegisterRepository

class EmailRegisterViewModel() : ViewModel() {

    fun registerWithEmail(
        userCredential: UserCredential,
        password: String,
        callback: (Boolean, String) -> Unit,
        emailVerification: (Boolean) -> Unit
    ) {
        RegisterRepository().registerUser(
            emailAddress = userCredential.email,
            password = password,
            callback = callback,
            emailVerified = emailVerification
        )
    }

    fun loginWithEmail(
        userCredential: UserCredential,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        RegisterRepository().loginUser(
            emailAddress = userCredential.email,
            password = password,
            callback = callback
        )
    }


}