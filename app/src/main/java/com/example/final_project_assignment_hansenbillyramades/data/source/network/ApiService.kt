package com.example.final_project_assignment_hansenbillyramades.data.source.network

import com.example.final_project_assignment_hansenbillyramades.data.model.OrderResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.ProductResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.TransactionResponse
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getAllProducts(
        @Query("search") search: String?,
        @Query("limit") limit: Int?
    ) : ProductResponse


    @GET("product/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductResponse

    @GET("products/category/{categoryName}")
    suspend fun getProductByCategory(@Path("categoryName") categoryName: String
    ): ProductResponse

    @GET("orders")
    suspend fun getAllTransactionOrders() : TransactionResponse

    @POST("order/snap")
    suspend fun createOrder(
        @Body orderRequest: Order
    ): OrderResponse
}