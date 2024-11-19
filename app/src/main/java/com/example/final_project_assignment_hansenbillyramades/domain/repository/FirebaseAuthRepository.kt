package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.domain.model.User

interface FirebaseAuthRepository {
    fun signOut()
    fun getUserInfo(): User?
}
