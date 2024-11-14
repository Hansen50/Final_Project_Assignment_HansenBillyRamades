package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartEntity
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.model.Item
import com.example.final_project_assignment_hansenbillyramades.domain.model.Order
import com.example.final_project_assignment_hansenbillyramades.domain.model.OrderState
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.repository.OrderRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
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
    private val db: StomazonDatabase,
) : ViewModel() {

    private val _cartList = MutableLiveData<List<CartEntity>>()
    val cartList: LiveData<List<CartEntity>> get() = _cartList

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

    fun createOrderFromCart() {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading
            try {
                val totalPrice = _totalPrice.value ?: 0f
                val cartItems = cartList.value ?: emptyList()
                val email = FirebaseAuth.getInstance().currentUser?.email ?: "default@example.com"
                val items = cartItems.map { cartItem ->
                    Item(
                        id = cartItem.productId,
                        name = cartItem.productName.take(10),
                        price = cartItem.productPrice,
                        quantity = cartItem.productQuantity
                    )
                }
                val orderRequest = Order(
                    amount = totalPrice.toInt(),
                    email = email,
                    items = items
                )

                val response = orderRepository.createOrder(orderRequest)
                Log.d("CekStatusOrderResponse", "StatusOrder: ${response.status}, Message: ${response.message}")

                if (response.status == "success") {
                    val paymentUrl = response.data?.transaction?.redirectUrl
                    val token = response.data?.transaction?.token

                    if (paymentUrl != null && token != null) {
                        // Gabungkan paymentUrl dengan token sebagai query parameter
                        val paymentUrlWithToken = "$paymentUrl?token=$token"
                        _orderState.value = OrderState.SuccessPayment(paymentUrlWithToken) // Kirimkan URL lengkap ke UI
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
