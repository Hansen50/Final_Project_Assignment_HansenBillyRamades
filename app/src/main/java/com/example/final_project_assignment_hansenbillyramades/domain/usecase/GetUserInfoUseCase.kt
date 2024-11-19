package com.example.final_project_assignment_hansenbillyramades.domain.usecase

import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.example.final_project_assignment_hansenbillyramades.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {

    suspend operator fun invoke(): User? {
        return firebaseAuthRepository.getUserInfo()
    }
}
