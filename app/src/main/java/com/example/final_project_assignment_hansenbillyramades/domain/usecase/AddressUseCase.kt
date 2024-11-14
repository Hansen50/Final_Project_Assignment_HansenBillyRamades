package com.example.final_project_assignment_hansenbillyramades.domain.usecase

import com.example.final_project_assignment_hansenbillyramades.domain.model.Address
import com.example.final_project_assignment_hansenbillyramades.domain.repository.AddressRepository
import javax.inject.Inject

class AddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository
) {

    // Menambahkan alamat
    suspend fun addAddress(address: Address) {
        addressRepository.insertAddress(address)
    }

    // Memperbarui alamat
    suspend fun updateAddress(address: Address) {
        addressRepository.updateAddress(address)
    }

    // Menghapus alamat
    suspend fun deleteAddress(address: Address) {
        addressRepository.deleteAddress(address)
    }

    // Mengambil semua alamat
    suspend fun getAllAddresses(): List<Address> {
        return addressRepository.getAllAddress()
    }
}
