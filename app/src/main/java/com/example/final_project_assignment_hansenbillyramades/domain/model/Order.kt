package com.example.final_project_assignment_hansenbillyramades.domain.model

data class Order(
    val amount: Int,
    val email: String,
    val items: List<Item>
)
