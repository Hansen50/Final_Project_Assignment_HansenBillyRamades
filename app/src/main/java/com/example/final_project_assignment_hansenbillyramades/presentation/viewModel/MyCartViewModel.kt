package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.CartState
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUeCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCartViewModel @Inject constructor(
    private val db: StomazonDatabase,
    private val cartUseCase: CartUeCase,
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState: StateFlow<CartState> get() = _cartState

    private val _isCartEmpty = MutableLiveData<Boolean>()
    val isCartEmpty: LiveData<Boolean> get() = _isCartEmpty

    private val _totalPrice = MutableLiveData<Float>()
    val totalPrice: LiveData<Float> get() = _totalPrice

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartState.Loading
            try {
                val cart = cartUseCase.getAllCart() // Mengambil cart dari use case
                if (cart.isNotEmpty()) {
                    _cartState.value = CartState.Success(cart)
                    calculateTotalPrice(cart)
                    _isCartEmpty.value = false
                } else {
                    _cartState.value = CartState.Error("Cart is empty")
                    _isCartEmpty.value = true
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Unknown error")
                _isCartEmpty.value = true
            }
        }
    }

    fun updateCart(cart: Cart) {
        viewModelScope.launch {
            try {
                cartUseCase.updateCart(cart)
                loadCart()  // Memuat ulang cart setelah update
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Failed to update cart")
            }
        }
    }

    fun calculateTotalPrice(cartList: List<Cart>) {
        var total = 0f
        cartList.forEach {
            total += it.cartPrice * it.quantity
        }
        _totalPrice.value = total
    }

    fun deleteCart(cart: Cart) {
        viewModelScope.launch {
            try {
                cartUseCase.deleteCart(cart)
                loadCart()  // Memuat ulang cart setelah penghapusan
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Failed to delete cart item")
            }
        }
    }
}