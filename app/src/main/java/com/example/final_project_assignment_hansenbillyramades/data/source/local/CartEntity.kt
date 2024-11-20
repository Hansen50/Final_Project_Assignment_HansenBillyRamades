package com.example.final_project_assignment_hansenbillyramades.data.source.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cart_entity")
data class CartEntity (
    @PrimaryKey
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "product_price") val productPrice: Int,
    @ColumnInfo(name = "product_quantity") var productQuantity: Int,
    @ColumnInfo(name = "image") val image: String,
)