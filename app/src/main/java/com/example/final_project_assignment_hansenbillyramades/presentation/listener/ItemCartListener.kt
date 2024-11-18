package com.example.final_project_assignment_hansenbillyramades.presentation.listener

import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart

interface ItemCartListener {
    fun onDelete(cart: Cart)
    fun onUpdateQuantity(cart: Cart)
    fun onIncrement(cart: Cart)
    fun onDecrement(cart: Cart)
}