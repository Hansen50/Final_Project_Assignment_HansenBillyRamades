package com.example.final_project_assignment_hansenbillyramades.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    @Insert
    suspend fun addCart(cartEntity: CartEntity)

    @Query("SELECT * FROM cart_entity")
    suspend fun getCart(): List<CartEntity>

    @Update
    suspend fun updateCart(cartEntity: CartEntity)

    @Delete
    suspend fun deleteCart(cartEntity: CartEntity)

    @Query("DELETE FROM cart_entity")
    suspend fun clearAllCarts()

    @Query("SELECT * FROM cart_entity WHERE product_id = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: Int): CartEntity?


}