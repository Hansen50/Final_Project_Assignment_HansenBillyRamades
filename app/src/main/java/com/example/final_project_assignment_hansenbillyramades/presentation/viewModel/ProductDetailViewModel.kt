package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUseCase
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetProductByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val cartUseCase: CartUseCase,
    ) : ViewModel() {

    private val _productState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productState: StateFlow<ProductsState> = _productState


    fun getProductDetail(id: Int) {
        viewModelScope.launch {
            _productState.value = ProductsState.Loading
            try {
                val productDetail = getProductByIdUseCase(id)
                _productState.value = ProductsState.SuccessDetail(productDetail)
            } catch (e: Exception) {
                _productState.value = ProductsState.Error(e.message ?: "An error occurred")
            }
        }
    }


    fun addCart(cart: Cart) {
        viewModelScope.launch {
            try {
                val existingCartItem = cartUseCase.getCartById(cart.id)
                if (existingCartItem != null) {
                    val updatedCart = existingCartItem.copy(quantity = existingCartItem.quantity + cart.quantity)
                    cartUseCase.updateCart(updatedCart)
                    _productState.value = ProductsState.AddedToCart(updatedCart)
                } else {
                    cartUseCase.addCart(cart)
                    _productState.value = ProductsState.AddedToCart(cart)
                }
            } catch (e: Exception) {
                _productState.value = ProductsState.Error("Error: ${e.message}")
            }
        }
    }
}



