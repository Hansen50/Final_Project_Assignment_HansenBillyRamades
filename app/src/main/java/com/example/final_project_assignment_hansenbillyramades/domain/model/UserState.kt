package com.example.final_project_assignment_hansenbillyramades.domain.model

sealed class UserState {
    data object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
}
