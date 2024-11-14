package com.example.final_project_assignment_hansenbillyramades.data.source.local

import javax.inject.Inject

class AddressLocalDataSourceImpl @Inject constructor(private val addressDao: AddressDao) : AddressLocalDataSource {
    override suspend fun getAllAddressItems(): List<AddressEntity> {
        return  addressDao.getAddress()
    }

    override suspend fun insertAddressItem(addressEntity: AddressEntity) {
        return addressDao.addAddress(addressEntity)
    }

    override suspend fun updateAddressItem(addressEntity: AddressEntity) {
        return addressDao.updateAddress(addressEntity)
    }

    override suspend fun deleteAddressItem(addressEntity: AddressEntity) {
        return addressDao.deleteAddress(addressEntity)
    }
}