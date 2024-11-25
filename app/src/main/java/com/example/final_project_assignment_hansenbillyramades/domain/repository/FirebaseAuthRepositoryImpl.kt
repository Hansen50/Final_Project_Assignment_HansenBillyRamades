package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.source.network.FirebaseAuthDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : FirebaseAuthRepository {

    override fun signOut() {
        firebaseAuthDataSource.signOut()
    }

    override suspend fun getUserInfo(): User? {
        val firebaseUser = firebaseAuthDataSource.getUserInfo()
        return firebaseUser?.let {
            User(
                name = it.displayName?: "No Name",
                email = it.email ?: "No Email",
                phone = it.phoneNumber ?: "",
                photoUrl = it.photoUrl?.toString()
            )
        }
    }
}
