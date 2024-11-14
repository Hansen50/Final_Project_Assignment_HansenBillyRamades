package com.example.final_project_assignment_hansenbillyramades.data.source.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "address_entity")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "address_recipient_name") val addressRecipientName: String,
    @ColumnInfo(name = "address_recipient_phone") val addressRecipientPhone: String,
    @ColumnInfo(name = "address_recipient_full_address") val addressRecipientFullAddress: String,
    @ColumnInfo(name = "address_recipient_zip_postal_code") val addressRecipientZipPostalCode: String
) : Parcelable