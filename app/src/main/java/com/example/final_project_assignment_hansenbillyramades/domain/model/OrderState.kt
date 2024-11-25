package com.example.final_project_assignment_hansenbillyramades.domain.model

import com.example.final_project_assignment_hansenbillyramades.data.model.OrderResponse


sealed class OrderState {
    object Loading : OrderState()
    data class Success(val orderResponse: OrderResponse) : OrderState()
    data class SuccessPayment(val paymentUrl: String) : OrderState()
    data class Error(val message: String) : OrderState()
}
