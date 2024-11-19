package com.example.final_project_assignment_hansenbillyramades.domain.usecase

import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.repository.CartRepository
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

    suspend fun addCart(cart: Cart) {
        cartRepository.insertCart(cart)
    }

    suspend fun updateCart(cart: Cart) {
        cartRepository.updateCart(cart)
    }

    suspend fun deleteCart(cart: Cart) {
        cartRepository.deleteCart(cart)
    }

    suspend fun getAllCart(): List<Cart> {
        return cartRepository.getAllCart()
    }

    suspend fun clearAllCart() {
        cartRepository.cleanCart()
    }
}