package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart

interface CartRepository {
    suspend fun getAllCart(): List<Cart>
    suspend fun getCartById(productId: Int): Cart?
    suspend fun insertCart(cart: Cart)
    suspend fun updateCart(cart: Cart)
    suspend fun deleteCart(cart: Cart)
    suspend fun cleanCart()
}