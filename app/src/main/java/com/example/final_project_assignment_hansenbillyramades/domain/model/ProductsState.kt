package com.example.final_project_assignment_hansenbillyramades.domain.model

sealed class ProductsState {
    data object Loading: ProductsState()
    data class Success(val products: List<Products>) : ProductsState()
    data class SuccessDetail(val product: Products) : ProductsState()
    data class Error(val message: String) : ProductsState()
    data class AddedToCart(val cartItem: Cart) : ProductsState()
}