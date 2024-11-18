package com.example.final_project_assignment_hansenbillyramades.domain.model

sealed class CartState {
    data object Loading: CartState()
    data class Success(val carts: List<Cart>) : CartState()
    data class Error(val message: String) : CartState()
}