package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionOrderDetailResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionResponse
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrder
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderDetail
import javax.inject.Inject

class TransactionOrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : TransactionOrderRepository {

    override suspend fun getAllTransactionOrder(): List<TransactionOrder> {
        return remoteDataSource.getAllTransactionOrders().data?.mapNotNull { it?.toTransaction() } ?: emptyList()
    }

    override suspend fun getTransactionOrdersById(id: String): TransactionOrderDetail {
        val response = remoteDataSource.getTransactionOrdersById(id)
        return response.data?.toDomainModel() ?: throw Exception("Data not found")
    }
}

fun TransactionResponse.Data.toTransaction(): TransactionOrder {
    return TransactionOrder(
        id = this.orPlatformId?: "",
        status = this.orStatus?: "",
        totalPrice = this.orTotalPrice?: 0
    )
}


fun TransactionOrderDetailResponse.Data.toDomainModel(): TransactionOrderDetail {
    return TransactionOrderDetail(
        id = this.orPlatformId?: "",
        status = this.orStatus.orEmpty(),
        totalPrice = this.orTotalPrice ?: 0,
        products = this.details?.flatMap { detail ->
            detail?.odProducts?.mapNotNull { product ->
                product?.toDomainModel()
            } ?: emptyList()
        } ?: emptyList()
    )
}

fun TransactionOrderDetailResponse.Data.Detail.OdProduct.toDomainModel(): TransactionOrderDetail.Product {
    return TransactionOrderDetail.Product(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        price = this.price ?: 0,
        quantity = this.quantity ?: 0,
        image = this.imageUrl?.pdImageUrl.orEmpty(),
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