package com.example.auction.data.repositories

import com.example.auction.data.models.UserCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterRepository {
    private val db = Firebase.firestore
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun loginUser(emailAddress: String, password: String, callback: (Boolean, String) -> Unit) {
        try {

            auth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login successful")
                } else {
                    val errorMessage = when (val exception = task.exception) {
                        is FirebaseAuthException -> {
                            when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "The email address is badly formatted."
                                "ERROR_WRONG_PASSWORD" -> "The password is incorrect."
                                "ERROR_USER_NOT_FOUND" -> "There is no user corresponding to this email."
                                "ERROR_USER_DISABLED" -> "The user account has been disabled by an administrator."
                                "ERROR_TOO_MANY_REQUESTS" -> "Too many requests. Try again later."
                                "ERROR_OPERATION_NOT_ALLOWED" -> "This operation is not allowed. Please enable it in the Firebase console."
                                else -> "Authentication failed. Please try again."
                            }
                        }

                        else -> "Authentication failed. Please try again."
                    }
                    callback(false, errorMessage)
                }
            }.addOnFailureListener {
                callback(false, it.message.toString())
            }
        } catch (e: Exception) {
            callback(false, e.message.toString())
        }
    }


    fun registerUser(
        emailAddress: String, password: String, callback: (Boolean, String) -> Unit
    ) {
        try {
            auth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                            val firebaseUserId = auth.currentUser?.uid
                            if (firebaseUserId != null) {
                                val user = UserCredential(emailAddress, userId = firebaseUserId)

                                db.collection("Login Details").document(firebaseUserId).get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            // User data already exists, no need to add it again
                                            callback(true, "User Already Exist")
                                        } else {
                                            // User data does not exist, add new data
                                            db.collection("Login Details").document(firebaseUserId)
                                                .set(user).addOnSuccessListener {
                                                    callback(true, "Register Success")
                                                }.addOnFailureListener {
                                                    callback(false, it.message.toString())
                                                }
                                        }
                                    }.addOnFailureListener {
                                        // Handle potential errors, e.g., network issues
                                        callback(false, it.message.toString())
                                    }
                            } else {
                                callback(false, "Already a user")
                            }
                        }
                    } else {
                        callback(false, "Register Unsuccessful")
                    }
                }
        } catch (e: Exception) {
            callback(false, e.message.toString())
        }
    }
}
