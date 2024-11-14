package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.ProductResponse
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : ProductRepository {
    override suspend fun getAllProducts(search: String?, limit: Int?): List<Products> {
        return remoteDataSource.getAllProducts(search, limit).data?.mapNotNull { it?.toProduct() } ?: emptyList()
    }

//    override suspend fun getProductById(id: Int): Products {
//        return remoteDataSource.getProductById(id)
//    }


    override suspend fun getProductByCategory(categoryName: String): List<Products> {
        val response = remoteDataSource.getProductByCategory(categoryName)

        return response.data?.flatMap { dataItem ->
            dataItem?.products?.map { product ->
                Products(
                    id = product?.pdId ?: 0,
                    name = product?.pdName ?: "",
                    price = product?.pdPrice ?: 0,
                    description = product?.pdDescription ?: "",
                    category = dataItem.categories?.firstOrNull()?.ctName ?: "No Category",
                    image = product?.pdImageUrl ?: "",
                    quantity = product?.pdQuantity ?: 0,
                    averageRating = product?.totalAverageRating ?: 0.0,
                    totalReviews = product?.totalReviews ?: ""
                )
            } ?: emptyList()
        } ?: emptyList()
    }
}

fun ProductResponse.Data.toProduct(): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = this.categories?.firstOrNull()?.ctName ?: "No Category",
        image = this.pdImageUrl ?: "",
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}
