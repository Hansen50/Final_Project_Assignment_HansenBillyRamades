package com.example.final_project_assignment_hansenbillyramades.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionOrder(
    val id: String,
    val status: String,
    val totalPrice: Int,
) : Parcelable




//package com.example.final_project_assignment_hansenbillyramades.domain.model
//
//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize
//
//@Parcelize
//data class TransactionOrder(
//    val id: Int,
//    val name: String,
//    val price: Int,
//    val totalPrice: Int,
//    val quantity: Int,
//) : Parcelable
