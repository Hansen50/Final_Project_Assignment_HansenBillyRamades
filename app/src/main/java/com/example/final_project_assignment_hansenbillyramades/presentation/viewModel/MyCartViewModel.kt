package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCartViewModel @Inject constructor(
    private val db: StomazonDatabase,
) : ViewModel() {

    private val _cartList = MutableLiveData<List<CartEntity>>()
    val cartList: LiveData<List<CartEntity>> get() = _cartList

    private val _isCartEmpty = MutableLiveData<Boolean>()
    val isCartEmpty: LiveData<Boolean> get() = _isCartEmpty

    private val _totalPrice = MutableLiveData<Float>()
    val totalPrice: LiveData<Float> get() = _totalPrice

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            val cart = db.cartDao().getCart()
            _cartList.value = cart
            _isCartEmpty.value = cart.isEmpty()
            calculateTotalPrice(cart)
        }
    }

    fun updateCart(cart: CartEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            db.cartDao().updateCart(cart)
            loadCart()
            onSuccess()
        }
    }

    fun calculateTotalPrice(cartList: List<CartEntity>) {
        var total = 0f
        cartList.forEach {
            total += it.productPrice * it.productQuantity
        }
        _totalPrice.value = total
    }

    fun deleteCart(cart: CartEntity) {
        viewModelScope.launch {
            db.cartDao().deleteCart(cart)
            loadCart()
        }
    }
}