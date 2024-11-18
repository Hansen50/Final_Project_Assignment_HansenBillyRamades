package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUeCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentWebViewModel @Inject constructor(
    private val cartUeCase: CartUeCase
) : ViewModel() {

    fun clearCart() {
        viewModelScope.launch {
            cartUeCase.clearAllCart()
        }
    }
}