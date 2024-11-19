package com.example.final_project_assignment_hansenbillyramades.data.source.network

import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.example.final_project_assignment_hansenbillyramades.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {

    override fun getUserInfo(): User? {
        return firebaseAuth.currentUser?.let {
            val firstName = it.displayName?.split(" ")?.first() ?: "No Name"
            User(
                name = firstName,
                email = it.email ?: "No Email",
                phone = it.phoneNumber ?: "",
                photoUrl = it.photoUrl?.toString()
            )
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}



