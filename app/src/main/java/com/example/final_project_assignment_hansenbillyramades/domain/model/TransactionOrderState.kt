package com.example.final_project_assignment_hansenbillyramades.domain.model

sealed class TransactionOrderState {
    data object Loading: TransactionOrderState()
    data class Success(val transactionOrder: List<TransactionOrder>) : TransactionOrderState()
    data class Error(val message: String) : TransactionOrderState()
}