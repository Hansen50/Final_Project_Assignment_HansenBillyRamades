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

    override suspend fun getAllTransactionOrder(orderPaymentStatus: String): List<TransactionOrder> {
        return remoteDataSource.getAllTransactionOrders(orderPaymentStatus).data?.mapNotNull { it?.toTransaction() } ?: emptyList()
        // data adalah response dari api yang menyimpan daftar transaksi order.
        // di sini sya juga menggunakan mapNotNull yang di mana berharap selama transformasi tidak mengandung nilai null, jadi ketika nilai null maka tidak akan mask ke list
        // kalau tetep ada data null maka akan mengembalikan emptylist saja
    }

    override suspend fun getTransactionOrdersById(id: String): TransactionOrderDetail {
        return remoteDataSource.getTransactionOrdersById(id).data?.toDomainModel() ?: throw Exception("Data not found")
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

//details adalah properti yang berisi daftar detail produk yang ada dalam transaksi.
// Ini mungkin berupa daftar objek yang berisi informasi produk.

//  Fungsi flatMap akan memproses setiap elemen detail dalam daftar details dan mengubahnya menjadi
//  elemen-elemen odProducts yang terdapat dalam setiap detail


//MapNotNull nah digunkakan untuk konversi setiap object produk ke dalama domian atau data class dan jika produk null maka ga di ambil
//jika odproduct bernilai null maka akan mengemalikan list kosong

fun TransactionOrderDetailResponse.Data.Detail.OdProduct.toDomainModel(): TransactionOrderDetail.Product {
    return TransactionOrderDetail.Product(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        price = this.price ?: 0,
        quantity = this.quantity ?: 0,
        image = this.imageUrl?.pdImageUrl.orEmpty(),
    )
}