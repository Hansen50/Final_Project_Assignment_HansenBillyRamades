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

    override fun getUserInfo(): User? {
        return firebaseAuthDataSource.getUserInfo()
    }
}
