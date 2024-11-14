package com.example.final_project_assignment_hansenbillyramades.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Products (
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val category: String,
    val image: String,
    val quantity: Int,
    val averageRating: Double,
    val totalReviews: String
) : Parcelable