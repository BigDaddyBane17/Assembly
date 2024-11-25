package com.example.computershop.presentation.manager_screens

import androidx.compose.runtime.Immutable
import com.example.computershop.domain.Order


@Immutable
data class ManagerOrderListState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val selectedOrder: Order? = null
)
