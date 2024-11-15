package com.example.final_project_assignment_hansenbillyramades.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Products(
    val id: Int = 0,
    val name: String? = null,
    val price: Int = 0,
    val description: String? = null,
    val category: String? = null,
    val image: String? = null,
    val quantity: Int = 0,
    val averageRating: Double = 0.0,
    val totalReviews: String? = null
) : Parcelable