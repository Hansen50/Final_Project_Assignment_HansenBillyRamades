package com.example.final_project_assignment_hansenbillyramades.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AddressDao {

    @Insert
    suspend fun addAddress(addressEntity: AddressEntity)

    @Query("SELECT * FROM address_entity")
    suspend fun getAddress(): List<AddressEntity>

    @Update
    suspend fun updateAddress(addressEntity: AddressEntity)

    @Delete
    suspend fun deleteAddress(addressEntity: AddressEntity)
}