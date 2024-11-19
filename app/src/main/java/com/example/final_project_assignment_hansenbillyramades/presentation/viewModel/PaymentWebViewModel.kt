package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentWebViewModel @Inject constructor(
    private val cartUseCase: CartUseCase
) : ViewModel() {

    fun clearCart() {
        viewModelScope.launch {
            cartUseCase.clearAllCart()
        }
    }
}