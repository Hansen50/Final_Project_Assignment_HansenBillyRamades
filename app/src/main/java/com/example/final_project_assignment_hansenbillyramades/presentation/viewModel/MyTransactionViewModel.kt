package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderState
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetListTransactionOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTransactionViewModel @Inject constructor(
    private val getListTransactionOrderUseCase: GetListTransactionOrderUseCase
) : ViewModel() {

    private val _transactionOrderState = MutableStateFlow<TransactionOrderState>(TransactionOrderState.Loading)
    val transactionOrderState: StateFlow<TransactionOrderState> = _transactionOrderState.asStateFlow()


    fun loadAllTransactionOrder(orderPaymentStatus: String) {
        _transactionOrderState.value = TransactionOrderState.Loading
        viewModelScope.launch {
            try {
                val transactionOrder = getListTransactionOrderUseCase(orderPaymentStatus)
                _transactionOrderState.value = TransactionOrderState.Success(transactionOrder)
            } catch (e: Exception) {
                _transactionOrderState.value = TransactionOrderState.Error(e.message ?: "Error")
            }
        }
    }
}
