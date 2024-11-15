package com.example.final_project_assignment_hansenbillyramades.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.domain.model.ProductsState
import com.example.final_project_assignment_hansenbillyramades.domain.model.TransactionOrderState
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetProductByIdUseCase
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.GetTransactionOrderDetailById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionOrderDetailViewModel @Inject constructor(
    private val getTransactionOrderDetailById: GetTransactionOrderDetailById,
    private val db: StomazonDatabase

) : ViewModel() {

    private val _transactionOrderState = MutableStateFlow<TransactionOrderState>(TransactionOrderState.Loading)
    val transactionOrderState: StateFlow<TransactionOrderState> = _transactionOrderState


    fun getTransactionOrderDetail(id: String) {
        viewModelScope.launch {
            _transactionOrderState.value = TransactionOrderState.Loading

            try {
                // Mengambil produk berdasarkan ID langsung
                val transactionOrderDetail = getTransactionOrderDetailById(id)
                if (transactionOrderDetail != null) {
                    _transactionOrderState.value = TransactionOrderState.SuccessDetail(transactionOrderDetail)
                } else {
                    _transactionOrderState.value = TransactionOrderState.Error("Order not found")
                }
            } catch (e: Exception) {
                _transactionOrderState.value = TransactionOrderState.Error(e.message ?: "An error occurred")
            }
        }
    }
}