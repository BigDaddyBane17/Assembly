package com.example.computershop.presentation.client_screens

import com.example.computershop.domain.Order

sealed interface OrderListAction {
    data class onOrderClick(val order: Order): OrderListAction
}