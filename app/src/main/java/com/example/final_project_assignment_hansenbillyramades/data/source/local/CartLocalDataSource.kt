package com.example.final_project_assignment_hansenbillyramades.data.source.local

import com.example.final_project_assignment_hansenbillyramades.data.source.local.room.CartEntity

interface CartLocalDataSource {

    suspend fun getAllCartItems(): List<CartEntity>
    suspend fun insertCartItem(cartEntity: CartEntity)
    suspend fun updateCartItem(cartEntity: CartEntity)
    suspend fun deleteCartItem(cartEntity: CartEntity)
    suspend fun clearAllCartItems()
    suspend fun getCartById(productId: Int): CartEntity?
}
