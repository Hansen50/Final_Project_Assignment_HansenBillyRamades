package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.CartState
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState: StateFlow<CartState> get() = _cartState


    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartState.Loading
            try {
                val cart = cartUseCase.getAllCart()
                val totalPrice = calculateTotalPrice(cart)
                _cartState.value = CartState.Success(cart, totalPrice)
                calculateTotalPrice(cart)
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun calculateTotalPrice(cartList: List<Cart>): Float {
        return cartList.sumOf { it.cartPrice * it.quantity }.toFloat()
    }

    fun deleteCart(cart: Cart) {
        viewModelScope.launch {
            try {
                cartUseCase.deleteCart(cart)
                loadCart()
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Failed to delete cart")
            }
        }
    }

    fun updateCart(cart: Cart) {
        viewModelScope.launch {
            try {
                cartUseCase.updateCart(cart)
                loadCart()
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Failed to update cart")
            }
        }
    }
}
