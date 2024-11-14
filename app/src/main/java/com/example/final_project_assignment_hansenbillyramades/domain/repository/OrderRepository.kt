package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.OrderResponse
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order

interface OrderRepository {
    suspend fun createOrder(orderRequest: Order): OrderResponse
}