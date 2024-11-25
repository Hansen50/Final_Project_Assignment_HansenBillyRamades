package com.example.final_project_assignment_hansenbillyramades.domain.usecase

import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.example.final_project_assignment_hansenbillyramades.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {

    fun invoke() {
        return firebaseAuthRepository.signOut()
    }
}