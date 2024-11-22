package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionOrderDetailResponse
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrder
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderDetail

interface TransactionOrderRepository {
    suspend fun getAllTransactionOrder(orderPaymentStatus: String): List<TransactionOrder>
    suspend fun getTransactionOrdersById(id: String): TransactionOrderDetail
}