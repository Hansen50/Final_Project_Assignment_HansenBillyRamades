package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.OrderResponse
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : OrderRepository{
    override suspend fun createOrder(orderRequest: Order): OrderResponse {
        return remoteDataSource.createOrder(orderRequest)
    }
}