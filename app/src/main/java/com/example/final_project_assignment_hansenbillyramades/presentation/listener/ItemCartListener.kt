package com.example.final_project_assignment_hansenbillyramades.presentation.listener

import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity

interface ItemCartListener {
    fun onDelete(cart: CartEntity)
    fun onUpdateQuantity(cart: CartEntity)
    fun onIncrement(cart: CartEntity)
    fun onDecrement(cart: CartEntity)
}