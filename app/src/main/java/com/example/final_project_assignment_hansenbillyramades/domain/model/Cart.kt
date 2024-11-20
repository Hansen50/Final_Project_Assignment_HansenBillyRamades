package com.example.final_project_assignment_hansenbillyramades.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val id: Int,
    val cartName: String,
    val cartPrice: Int,
    var quantity: Int,
    val cartImage: String
) : Parcelable
