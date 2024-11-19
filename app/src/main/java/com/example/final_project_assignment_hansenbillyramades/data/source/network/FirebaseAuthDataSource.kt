package com.example.final_project_assignment_hansenbillyramades.data.source.network

import com.example.final_project_assignment_hansenbillyramades.domain.model.User

interface FirebaseAuthDataSource {
    fun signOut()
    fun getUserInfo(): User?
}