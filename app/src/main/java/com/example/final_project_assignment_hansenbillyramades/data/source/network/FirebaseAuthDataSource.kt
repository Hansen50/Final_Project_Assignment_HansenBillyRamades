package com.example.final_project_assignment_hansenbillyramades.data.source.network

import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthDataSource {
    fun signOut()
    suspend fun getUserInfo(): FirebaseUser?
}