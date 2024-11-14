package com.example.final_project_assignment_hansenbillyramades.presentation.listener

import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressEntity
import com.example.final_project_assignment_hansenbillyramades.domain.model.Address

interface ItemAddressListener {
    fun onDelete(address: AddressEntity)
    fun onEdit(address: AddressEntity)
}