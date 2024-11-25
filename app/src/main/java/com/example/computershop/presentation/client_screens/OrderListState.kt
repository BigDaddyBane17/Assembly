package com.example.computershop.presentation.client_screens

import androidx.compose.runtime.Immutable
import com.example.computershop.domain.Order


@Immutable
data class OrderListState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null
)
