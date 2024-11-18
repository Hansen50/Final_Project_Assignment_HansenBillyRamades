package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUeCase
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val cartUseCase: CartUeCase,
    private val db: StomazonDatabase,

    ) : ViewModel() {

    private val _productState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productState: StateFlow<ProductsState> = _productState


    fun getProductDetail(id: Int) {
        viewModelScope.launch {
            _productState.value = ProductsState.Loading

            try {
                val productDetail = getProductByIdUseCase(id)
                if (productDetail != null) {
                    _productState.value = ProductsState.SuccessDetail(productDetail)
                } else {
                    _productState.value = ProductsState.Error("Product not found")
                }
            } catch (e: Exception) {
                _productState.value = ProductsState.Error(e.message ?: "An error occurred")
            }
        }
    }


    fun addCart(cart: CartEntity) {
        viewModelScope.launch {
            try {
                cartUseCase.addCart(
                    Cart(
                        cart.productId,
                        cart.productName,
                        cart.productPrice,
                        cart.productQuantity,
                        cart.image
                    )
                )
            } catch (e: Exception) {
            }
        }
    }
}


