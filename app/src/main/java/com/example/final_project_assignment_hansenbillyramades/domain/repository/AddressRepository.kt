package com.example.final_project_assignment_hansenbillyramades.domain.repository
import com.example.final_project_assignment_hansenbillyramades.domain.model.Address

interface AddressRepository {
    suspend fun getAllAddress(): List<Address>
    suspend fun insertAddress(address: Address)
    suspend fun updateAddress(address: Address)
    suspend fun deleteAddress(address: Address)
}
