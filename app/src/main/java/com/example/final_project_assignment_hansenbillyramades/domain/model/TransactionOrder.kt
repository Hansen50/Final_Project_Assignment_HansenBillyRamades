package com.example.final_project_assignment_hansenbillyramades.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionOrder(
    val id: String,
    val dateOrder: String,
    val status: String,
    val totalPrice: Int,
) : Parcelable
