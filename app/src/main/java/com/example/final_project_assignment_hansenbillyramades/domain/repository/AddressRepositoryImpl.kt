package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressLocalDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.Address
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val localDataSource: AddressLocalDataSource
) : AddressRepository {

    override suspend fun getAllAddress(): List<Address> {
        return localDataSource.getAllAddressItems().map { it.toDomainModel() }
    }

    override suspend fun insertAddress(address: Address) {
        localDataSource.insertAddressItem(address.toEntityModel())
    }

    override suspend fun updateAddress(address: Address) {
        localDataSource.updateAddressItem(address.toEntityModel())
    }

    override suspend fun deleteAddress(address: Address) {
        localDataSource.deleteAddressItem(address.toEntityModel())
    }

    private fun AddressEntity.toDomainModel(): Address {
        return Address(
            id = id,
            recipientName = addressRecipientName,
            recipientPhone = addressRecipientPhone,
            recipientFullAddress = addressRecipientFullAddress,
            recipientZipPostalCode = addressRecipientZipPostalCode
        )
    }

    private fun Address.toEntityModel(): AddressEntity {
        return AddressEntity(
            id = id,
            addressRecipientName = recipientName,
            addressRecipientPhone = recipientPhone,
            addressRecipientFullAddress = recipientFullAddress,
            addressRecipientZipPostalCode = recipientZipPostalCode
        )
    }
}
