package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderState
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetTransactionOrderDetailById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionOrderDetailViewModel @Inject constructor(
    private val getTransactionOrderDetailById: GetTransactionOrderDetailById,

) : ViewModel() {

    private val _transactionOrderState = MutableStateFlow<TransactionOrderState>(TransactionOrderState.Loading)
    val transactionOrderState: StateFlow<TransactionOrderState> = _transactionOrderState


    fun getTransactionOrderDetail(id: String) {
        viewModelScope.launch {
            _transactionOrderState.value = TransactionOrderState.Loading

            try {
                val transactionOrderDetail = getTransactionOrderDetailById(id)
                _transactionOrderState.value = TransactionOrderState.SuccessDetail(transactionOrderDetail)
            } catch (e: Exception) {
                _transactionOrderState.value = TransactionOrderState.Error(e.message ?: "An error occurred")
            }
        }
    }
}