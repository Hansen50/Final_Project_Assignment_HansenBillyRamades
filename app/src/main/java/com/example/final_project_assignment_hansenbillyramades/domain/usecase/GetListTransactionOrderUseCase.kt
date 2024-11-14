package com.example.final_project_assignment_hansenbillyramades.domain.usecase

import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrder
import com.example.final_project_assignment_hansenbillyramades.domain.repository.TransactionOrderRepository
import javax.inject.Inject

class GetListTransactionOrderUseCase @Inject constructor(
    private val transactionOrderRepository: TransactionOrderRepository
) {
    suspend operator fun invoke() : List<TransactionOrder> {
        return transactionOrderRepository.getAllTransactionOrder()
    }

}