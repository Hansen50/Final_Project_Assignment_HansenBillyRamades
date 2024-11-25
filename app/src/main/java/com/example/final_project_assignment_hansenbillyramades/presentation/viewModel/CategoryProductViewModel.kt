package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.model.Products
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetProductsByCategoryUseCase
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.ListProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _productsState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> get() = _productsState


    fun getProductsByCategory(categoryName: String, search: String) {
        viewModelScope.launch {
            _productsState.value = ProductsState.Loading
            try {
                val products = getProductsByCategoryUseCase(categoryName, search)
                _productsState.value = if (products.isNotEmpty()) {
                    ProductsState.Success(products)
                } else {
                    ProductsState.NoResults("No products found for your search")
                }
            } catch (e: Exception) {
                _productsState.value = ProductsState.Error("Error: ${e.message}")
            }
        }
    }
}



