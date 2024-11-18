package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.model.Cart
import com.example.final_project_assignment_hansenbillyramades.domain.model.CartState
import com.example.final_project_assignment_hansenbillyramades.domain.model.Item
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order
import com.example.final_project_assignment_hansenbillyramades.domain.model.OrderState
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.repository.OrderRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUeCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val cartUseCase: CartUeCase,
    private val db: StomazonDatabase,
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartState>(CartState.Loading)
    val cartState: StateFlow<CartState> get() = _cartState

    private val _isCartEmpty = MutableLiveData<Boolean>()
    val isCartEmpty: LiveData<Boolean> get() = _isCartEmpty

    private val _totalPrice = MutableLiveData<Float>()
    val totalPrice: LiveData<Float> get() = _totalPrice

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Loading)
    val orderState: StateFlow<OrderState> get() = _orderState

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
                } else {
                    _cartState.value = CartState.Error("Cart is empty")
                }
            } catch (e: Exception) {
                _cartState.value = CartState.Error(e.message ?: "Unknown error")
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

        fun createOrderFromCart() {
            viewModelScope.launch {
                _orderState.value = OrderState.Loading
                try {
                    val totalPrice = _totalPrice.value ?: 0f
                    val cartItems = (cartState.value as? CartState.Success)?.carts ?: emptyList()
                    val email =
                        FirebaseAuth.getInstance().currentUser?.email ?: "default@example.com"
                    val items = cartItems.map { cartItem ->
                        Log.d("coba", cartItems.toString())
                        Item(
                            id = cartItem.id,
                            name = cartItem.cartName.take(10),
                            price = cartItem.cartPrice,
                            quantity = cartItem.quantity
                        )
                    }
                    val orderRequest = Order(
                        amount = totalPrice.toInt(),
                        email = email,
                        items = items
                    )

                    val response = orderRepository.createOrder(orderRequest)
                    Log.d(
                        "CekStatusOrderResponse",
                        "StatusOrder: ${response.status}, Message: ${response.message}"
                    )

                    if (response.status == "success") {
                        val paymentUrl = response.data?.transaction?.redirectUrl
                        val token = response.data?.transaction?.token

                        if (paymentUrl != null && token != null) {
                            val paymentUrlWithToken = "$paymentUrl?token=$token"
                            _orderState.value =
                                OrderState.SuccessPayment(paymentUrlWithToken) // Kirimkan URL lengkap ke UI
                        } else {
                            _orderState.value = OrderState.Success(response)
                        }
                    } else {
                        _orderState.value = OrderState.Error("Order creation failed")
                    }

                } catch (e: Exception) {
                    _orderState.value = OrderState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

