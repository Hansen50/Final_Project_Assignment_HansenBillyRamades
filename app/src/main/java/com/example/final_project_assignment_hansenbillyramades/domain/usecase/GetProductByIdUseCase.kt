package com.example.final_project_assignment_hansenbillyramades.domain.usecase

import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Products {
        return productRepository.getProductById(id)
    }
}
