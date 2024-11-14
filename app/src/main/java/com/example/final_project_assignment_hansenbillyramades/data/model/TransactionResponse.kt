package com.example.final_project_assignment_hansenbillyramades.data.model


import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("totalOrders")
    val totalOrders: Int?,
    @SerializedName("totalPages")
    val totalPages: Int?
) {
    data class Data(
        @SerializedName("details")
        val details: List<Detail?>?,
        @SerializedName("or_id")
        val orId: Int?,
        @SerializedName("or_payment_status")
        val orPaymentStatus: String?,
        @SerializedName("or_platform_id")
        val orPlatformId: String?,
        @SerializedName("or_status")
        val orStatus: String?,
        @SerializedName("or_token_id")
        val orTokenId: String?,
        @SerializedName("or_total_price")
        val orTotalPrice: Int?,
        @SerializedName("promos")
        val promos: List<Any?>?
    ) {
        data class Detail(
            @SerializedName("od_products")
            val odProducts: List<OdProduct?>?
        ) {
            data class OdProduct(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("price")
                val price: Int?,
                @SerializedName("quantity")
                val quantity: Int?
            )
        }
    }
}