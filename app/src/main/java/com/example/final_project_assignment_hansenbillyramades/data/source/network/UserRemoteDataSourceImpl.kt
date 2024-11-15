package com.example.final_project_assignment_hansenbillyramades.data.source.network

import android.util.Log
import com.example.final_project_assignment_hansenbillyramades.data.model.OrderResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.ProductDetailResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.ProductResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionOrderDetailResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionResponse
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
UserRemoteDataSource{
    override suspend fun getAllProducts(search: String?, limit: Int?): ProductResponse {
        return apiService.getAllProducts(search, limit)
    }

    override suspend fun getProductById(id: Int): ProductDetailResponse {
        return apiService.getProductById(id)
    }

    override suspend fun getProductByCategory(categoryName: String, search: String?): ProductResponse {
        return apiService.getProductByCategory(categoryName, search)
    }

    override suspend fun createOrder(orderRequest: Order): OrderResponse {
        return apiService.createOrder(orderRequest)
    }

    override suspend fun getAllTransactionOrders(): TransactionResponse {
        return apiService.getAllTransactionOrders()
    }

    override suspend fun getTransactionOrdersById(id: String): TransactionOrderDetailResponse {
        return apiService.getTransactionOrdersById(id)
    }
}