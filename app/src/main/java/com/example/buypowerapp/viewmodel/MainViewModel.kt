package com.example.buypowerapp.viewmodel

import com.example.buypowerapp.util.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) {
    fun getUserFirstName(
        userId: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userCollection = fireStore.collection(Constant.COLLECTION_PATH)
        userCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val fullName = documentSnapshot.getString(Constant.USER_FULL_NAME)
                    val first = fullName?.split(" ")?.get(0) ?: ""
                    onSuccess(first)
                } else {
                    onFailure("User document does not exist")
                }
            }.addOnFailureListener { exception ->
                onFailure(exception.message ?: "Unknown error")
            }
    }
}