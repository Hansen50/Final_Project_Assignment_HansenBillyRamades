package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.model.UserState
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.model.User
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetUserInfoUseCase
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.ListProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val listProductUseCase: ListProductUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> = _productState.asStateFlow()


    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    private var isLoading = false

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


    fun getUserInfo() {
        viewModelScope.launch {
            try {
                val user = getUserInfoUseCase()
                if (user != null) {
                    _userState.value = UserState.Success(user)
                } else {
                    _userState.value = UserState.Error("User not logged in")
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Error")
            }
        }
    }
}
