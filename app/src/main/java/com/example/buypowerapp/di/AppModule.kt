package com.example.buypowerapp.di

import com.example.buypowerapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun provideAuthViewModel(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore,
    ): AuthViewModel {
        return AuthViewModel(
            firebaseAuth,
            fireStore
        )
    }
}