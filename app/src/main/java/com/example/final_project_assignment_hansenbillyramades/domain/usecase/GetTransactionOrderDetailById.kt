package com.example.final_project_assignment_hansenbillyramades.domain.usecase

import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderDetail
import com.example.final_project_assignment_hansenbillyramades.domain.repository.TransactionOrderRepository
import javax.inject.Inject

class GetTransactionOrderDetailById @Inject constructor(
    private val transactionOrderRepository: TransactionOrderRepository
){
    suspend operator fun invoke(id: String): TransactionOrderDetail {
        return transactionOrderRepository.getTransactionOrdersById(id)
    }
}