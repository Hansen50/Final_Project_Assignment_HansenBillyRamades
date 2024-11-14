package com.example.final_project_assignment_hansenbillyramades.domain.repository

import com.example.final_project_assignment_hansenbillyramades.data.model.ProductResponse
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products

interface ProductRepository {
    suspend fun getAllProducts(search: String?, limit: Int?): List<Products>
    //suspend fun getProductById(id: Int): Products
    suspend fun getProductByCategory(categoryName: String): List<Products>

}