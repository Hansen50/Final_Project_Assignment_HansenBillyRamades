package com.example.final_project_assignment_hansenbillyramades.domain.model

sealed class AddressState {
    data object Loading: AddressState()
    data class Success(val address: List<Address>) : AddressState()
    data class Error(val message: String) : AddressState()
}