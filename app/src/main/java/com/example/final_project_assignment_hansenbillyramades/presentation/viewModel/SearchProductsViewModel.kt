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
class SearchProductsViewModel @Inject constructor(
    private val listProductUseCase: ListProductUseCase,
) : ViewModel() {

    private val _productState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> = _productState.asStateFlow()


    fun loadAllProducts(search: String?, limit: Int?) {
        _productState.value = ProductsState.Loading
        viewModelScope.launch {
            try {
                val products = listProductUseCase(search, limit)
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
}