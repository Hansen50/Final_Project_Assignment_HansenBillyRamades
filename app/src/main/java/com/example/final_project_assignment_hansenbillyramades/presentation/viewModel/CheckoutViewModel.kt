package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.model.OrderResponse
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.CartState
import com.example.final_project_assignment_hansenbillyramades.domain.model.Item
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order
import com.example.final_project_assignment_hansenbillyramades.domain.model.OrderState
import com.example.final_project_assignment_hansenbillyramades.domain.repository.OrderRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartUseCase: CartUseCase,
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState: StateFlow<CartState> get() = _cartState

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Loading)
    val orderState: StateFlow<OrderState> get() = _orderState

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartState.Loading
            try {
                val cart = cartUseCase.getAllCart()
                val totalPrice = calculateTotalPrice(cart)
                _cartState.value = CartState.Success(cart, totalPrice)
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Unknown error")
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

    fun deleteCart(cart: Cart) {
        viewModelScope.launch {
            try {
                cartUseCase.deleteCart(cart)
                loadCart()
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Failed to delete cart item")
            }
        }
    }

    private fun calculateTotalPrice(cartList: List<Cart>): Float {
        return cartList.sumOf { it.cartPrice * it.quantity }.toFloat()
    }

    fun createOrderFromCart() {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading
            try {
                val cartStateSuccess = (cartState.value as? CartState.Success)
                val totalPrice = cartStateSuccess?.totalPrice ?: 0f
                val cartItems = cartStateSuccess?.carts ?: emptyList()
                val email = FirebaseAuth.getInstance().currentUser?.email ?: "default@example.com"
                val items = mapCartItemsToOrderItems(cartItems)

                val orderRequest = Order(amount = totalPrice.toInt(), email = email, items = items)
                processOrder(orderRequest)
            } catch (e: Exception) {
                _orderState.value = OrderState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun mapCartItemsToOrderItems(cartItems: List<Cart>): List<Item> {
        return cartItems.map { cartItem ->
            Item(
                id = cartItem.id,
                name = cartItem.cartName.take(50),
                price = cartItem.cartPrice,
                quantity = cartItem.quantity
            )
        }
    }

    private suspend fun processOrder(orderRequest: Order) {
        val response = orderRepository.createOrder(orderRequest)

        if (response.status == "success") {
            handleSuccessfulOrder(response)
        } else {
            _orderState.value = OrderState.Error("Order creation failed")
        }
    }

    private fun handleSuccessfulOrder(response: OrderResponse) {
        val paymentUrl = response.data?.transaction?.redirectUrl
        val token = response.data?.transaction?.token

        if (paymentUrl != null && token != null) {
            val paymentUrlWithToken = "$paymentUrl?token=$token"
            _orderState.value = OrderState.SuccessPayment(paymentUrlWithToken)
        } else {
            _orderState.value = OrderState.Success(response)
        }
    }
}


