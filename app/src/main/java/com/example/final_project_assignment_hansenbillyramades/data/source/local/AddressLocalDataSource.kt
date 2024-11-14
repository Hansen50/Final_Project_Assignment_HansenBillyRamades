package com.example.final_project_assignment_hansenbillyramades.data.source.local

interface AddressLocalDataSource {

    suspend fun getAllAddressItems(): List<AddressEntity>
    suspend fun insertAddressItem(addressEntity: AddressEntity)
    suspend fun updateAddressItem(addressEntity: AddressEntity)
    suspend fun deleteAddressItem(addressEntity: AddressEntity)

}