package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartLocalDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.Address
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val localDataSource: CartLocalDataSource
)  : CartRepository {
    override suspend fun getAllCart(): List<Cart> {
        return localDataSource.getAllCartItems().map { it.toDomainModel() }
    }

    override suspend fun getCartById(productId: Int): Cart? {
        return localDataSource.getCartById(productId)?.toDomainModel()
    }


    override suspend fun insertCart(cart: Cart) {
        localDataSource.insertCartItem(cart.toEntityModel())
    }

    override suspend fun updateCart(cart: Cart) {
        localDataSource.updateCartItem(cart.toEntityModel())
    }

    override suspend fun deleteCart(cart: Cart) {
        localDataSource.deleteCartItem(cart.toEntityModel())
    }

    override suspend fun cleanCart() {
        localDataSource.clearAllCartItems()
    }

    fun CartEntity.toDomainModel(): Cart {
        return Cart(
            id = productId,
            cartName = productName,
            cartPrice = productPrice,
            quantity = productQuantity,
            cartImage = image
        )
    }

    fun Cart.toEntityModel(): CartEntity {
        return CartEntity(
            productId = id,
            productName = cartName,
            productPrice = cartPrice,
            productQuantity = quantity,
            image = cartImage
        )
    }
}