package com.example.final_project_assignment_hansenbillyramades.data.source.local

import com.example.final_project_assignment_hansenbillyramades.data.source.local.room.CartDao
import com.example.final_project_assignment_hansenbillyramades.data.source.local.room.CartEntity
import javax.inject.Inject

class CartLocalDataSourceImpl @Inject constructor(private val cartDao: CartDao) : CartLocalDataSource {

    override suspend fun getAllCartItems(): List<CartEntity> {
        return cartDao.getCart()
    }

    override suspend fun insertCartItem(cartEntity: CartEntity) {
        cartDao.addCart(cartEntity)
    }

    override suspend fun updateCartItem(cartEntity: CartEntity) {
        cartDao.updateCart(cartEntity)
    }

    override suspend fun deleteCartItem(cartEntity: CartEntity) {
        cartDao.deleteCart(cartEntity)
    }

    override suspend fun clearAllCartItems() {
        cartDao.clearAllCarts()
    }

    override suspend fun getCartById(productId: Int): CartEntity? {
        return cartDao.getCartItemByProductId(productId)
    }
}
