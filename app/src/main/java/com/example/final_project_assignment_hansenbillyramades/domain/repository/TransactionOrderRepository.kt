package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrder

interface TransactionOrderRepository {
    suspend fun getAllTransactionOrder(): List<TransactionOrder>
}