package com.example.final_project_assignment_hansenbillyramades.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionOrderDetail(
    val id: String,
    val dateOrder: String,
    val status: String,
    val totalPrice: Int,
    val products: List<Product>,
) : Parcelable {
    @Parcelize
    data class Product(
        val id: Int,
        val name: String,
        val price: Int,
        val quantity: Int,
        val image: String
    ) : Parcelable
}
