package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.ProductDetailResponse
import com.example.final_project_assignment_hansenbillyramades.data.model.ProductResponse
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products

import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : ProductRepository {
    override suspend fun getAllProducts(search: String?): List<Products> {
        return remoteDataSource.getAllProducts(search).data?.mapNotNull { it?.toProduct() } ?: emptyList()
    }

    override suspend fun getProductById(id: Int): Products {
        return remoteDataSource.getProductById(id).data?.toProductDetail() ?: throw Exception("Product Not Found")
    }



    override suspend fun getProductByCategory(categoryName: String, search: String): List<Products> {
        return remoteDataSource.getProductByCategory(categoryName, search).data?.flatMap { dataItem ->
            dataItem?.products?.mapNotNull { product ->
                product?.toProductByCategory(
                    dataItem.categories?.firstOrNull()?.ctName ?: "No Category"
                )
            } ?: emptyList()
        } ?: emptyList()
    }

}

fun ProductResponse.Data.Product.toProductByCategory(category: String): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = category,
        images = listOfNotNull(this.pdImageUrl),
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}


fun ProductResponse.Data.toProduct(): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = this.categories?.firstOrNull()?.ctName ?: "No Category",
        images = listOfNotNull(this.pdImageUrl),
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}

fun ProductDetailResponse.Data.toProductDetail(): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = this.categories?.firstOrNull()?.ctName ?: "No Category",
        images = listOfNotNull(
            this.pdImageUrl,
            this.pdData?.imageUrl2,
            this.pdData?.imageUrl3
        ),
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}


