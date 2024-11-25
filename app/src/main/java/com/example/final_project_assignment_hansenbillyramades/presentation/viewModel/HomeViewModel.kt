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
    // mutablestateflow digunakan untuk meyimpan status atau state

    //MutableStateFlow: Data bisa diubah (mutable) di dalam ViewModel. Cocok untuk mengelola state aplikasi.
    //StateFlow: Data hanya bisa dibaca (read-only) oleh UI. Cocok untuk memberikan data ke UI secara aman tanpa risiko modifikasi.


    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    // mutable live data digunakan menyimpan data pengguna sebagai objek bukan state
    // karena pake live data di akses otmats berubah di ui

    init {
        loadAllProducts(null)
        getUserInfo()
    }


    fun loadAllProducts(name: String?) {
        _productState.value = ProductsState.Loading
        viewModelScope.launch {
            try {
                val products = listProductUseCase(name)
                _productState.value = if (products.isNotEmpty()) {
                    ProductsState.Success(products)
                } else {
                    ProductsState.Error("No more products found")
                }
            } catch (e: Exception) {
                _productState.value = ProductsState.Error(e.message ?: "Error")
            }
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
