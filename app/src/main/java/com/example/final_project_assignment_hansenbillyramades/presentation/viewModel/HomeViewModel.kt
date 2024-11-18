package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.ListProductUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listProductUseCase: ListProductUseCase,
) : ViewModel() {

    private val _productState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> = _productState.asStateFlow()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //var currentLimit = 4

    private var isLoading = false


    init {
        loadAllProducts(null, null)
        getUserInfo()
    }

    fun loadAllProducts(name: String?, limit: Int?) {
        if (isLoading) return

        isLoading = true
        _productState.value = ProductsState.Loading

        viewModelScope.launch {
            try {
                val products = listProductUseCase(name, limit)
                _productState.value = if (products.isNotEmpty()) {
                    ProductsState.Success(products)
                } else {
                    ProductsState.Error("No more products found")
                }
            } catch (e: Exception) {
                _productState.value = ProductsState.Error(e.message ?: "Error")
            }
            isLoading = false
        }
    }


    private fun getUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            val displayName = user.displayName
            val firstName = displayName?.split(" ")?.firstOrNull() ?: "No name available"
            val email = user.email ?: "No email available"
            val phone = user.phoneNumber ?: "No phone available"
            val photoUrl = user.photoUrl

            _user.value = User(firstName, email, phone, null)
        } else {
            _user.value = User("User not logged in", "", "", null)
        }
    }

//    fun increaseLimit() {
//        currentLimit += 4// Atur jumlah produk per halaman (misalnya menambah 5)
//    }
}