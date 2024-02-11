package com.example.buypowerapp.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buypowerapp.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : ViewModel() {

    private val currentUserLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    companion object {
        const val FIRE_STORE_TAG = "FireStore"
        const val USER_DETAILS_SAVED = "User details saved"
        const val USER_DETAILS_NOT_SAVED = "User details not saved"
        const val CREATE_USER_FAILURE = "createUserWithEmail:failure"
    }

    init {
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            currentUserLiveData.value = firebaseAuth.currentUser
        }
    }

    fun createUserWithEmailAndPassword(
        userEmail: String,
        password: String,
        fullName: String,
        phoneNumber: String, onComplete: (Boolean, Exception?, String?) -> Unit
    ) {
        firebaseAuth.fetchSignInMethodsForEmail(userEmail)
            .addOnCompleteListener { emailTask ->
                if (emailTask.isSuccessful) {
                    val signInMethod = emailTask.result.signInMethods
                    if (!signInMethod.isNullOrEmpty()) {
                        onComplete(false, null, Constant.EMAIL_ALREADY_EXIST)
                    } else {
                        // The email and phone number are not in use, proceed with user creation
                        firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = firebaseAuth.currentUser!!
                                    saveUserDetailsToFireStore(
                                        user.uid,
                                        fullName,
                                        userEmail,
                                        phoneNumber
                                    )
                                    onComplete(true, null, null)
                                } else {
                                    onComplete(false, task.exception, null)
                                    Log.w(
                                        ContentValues.TAG,
                                        CREATE_USER_FAILURE,
                                        task.exception
                                    )
                                }
                            }.addOnFailureListener { exception ->
                                val errorMessage = when (exception) {
                                    is FirebaseAuthException -> {
                                        Constant.USER_CREATION_FAILED
                                    }

                                    else -> {
                                        Constant.ERROR_OCCURRED
                                    }
                                }
                                onComplete(false, exception, errorMessage)
                            }
                    }
                }
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String, onComplete: (Boolean, String?) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, null)
                }
            }.addOnFailureListener { exception ->
                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidUserException -> {
                        // Handle invalid user (user does not exist) error
                        // Show a relevant error message to the user
                        Constant.USER_DOES_NOT_EXIST
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Handle invalid credentials (wrong password) error
                        // Show a relevant error message to the user
                        Constant.INVALID_CREDENTIALS
                    }
                    is FirebaseAuthException -> {
                        // Handle other FirebaseAuthExceptions
                        // Show a generic error message or log the exception for debugging
                        Constant.AUTHENTICATION_FAILED
                    }
                    else -> {
                        Constant.ERROR_OCCURRED
                    }
                }
                onComplete(false, errorMessage)
            }
    }

    private fun saveUserDetailsToFireStore(
        userId: String,
        fullName: String,
        email: String,
        phoneNumber: String
    ) {
        val user = hashMapOf(
            Constant.userFullName to fullName,
            Constant.userEmail to email,
            Constant.userPhoneNumber to phoneNumber
        )
        fireStore.collection(Constant.COLLECTION_PATH)
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                // Successfully saved user details to FireStore
                Log.d(
                    FIRE_STORE_TAG,
                    USER_DETAILS_SAVED
                )
            }
            .addOnFailureListener { e ->
                // Handle the failure to save user details to FireStore
                Log.e(
                    FIRE_STORE_TAG,
                    USER_DETAILS_NOT_SAVED, e
                )
            }
    }
}
