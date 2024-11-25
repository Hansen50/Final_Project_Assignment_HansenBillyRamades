package com.example.final_project_assignment_hansenbillyramades.data.source.network

import com.example.final_project_assignment_hansenbillyramades.data.model.OrderResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.ProductDetailResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.ProductResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionOrderDetailResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionResponse
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products

interface UserRemoteDataSource {
    suspend fun getAllProducts(search: String?) : ProductResponse
    suspend fun getProductById(id: Int) : ProductDetailResponse
    suspend fun getProductByCategory(categoryName: String, search: String?) : ProductResponse
    suspend fun createOrder(orderRequest: Order): OrderResponse
    suspend fun getAllTransactionOrders(orderPaymentStatus: String) : TransactionResponse
    suspend fun getTransactionOrdersById(id: String) : TransactionOrderDetailResponse

}