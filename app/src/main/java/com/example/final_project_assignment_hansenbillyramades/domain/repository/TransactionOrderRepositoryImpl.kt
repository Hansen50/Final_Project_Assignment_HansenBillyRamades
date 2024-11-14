package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionResponse
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrder
import javax.inject.Inject

class TransactionOrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : TransactionOrderRepository {

    override suspend fun getAllTransactionOrder(): List<TransactionOrder> {
        return remoteDataSource.getAllTransactionOrders().data?.mapNotNull { it?.toTransaction() } ?: emptyList()
    }
    }

fun TransactionResponse.Data.toTransaction(): TransactionOrder {
    return TransactionOrder(
        id = this.orId?: 0,
        status = this.orStatus?: "",
        totalPrice = this.orTotalPrice?: 0
    )
}


//fun TransactionResponse.Data.toTransaction(): TransactionOrder {
//    return TransactionOrder(
//        id = this.id ?: 0,
//        name = this.name ?: "",
//        price = this.price?: 0,
//        quantity = this.quantity?: 0,
//        totalPrice = (this.price ?: 0) * (this.quantity ?: 0)
//
//    )
//}