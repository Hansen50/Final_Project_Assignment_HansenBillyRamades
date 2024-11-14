package com.example.final_project_assignment_hansenbillyramades.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: Int = 0,
    val recipientName: String,
    val recipientPhone: String,
    val recipientFullAddress: String,
    val recipientZipPostalCode: String
) : Parcelable
