package com.example.computershop.presentation.client_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computershop.domain.Order
import com.example.computershop.domain.OrderDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderListViewModel(
    private val orderDataSource: OrderDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(OrderListState())

    val state = _state
        .onStart { loadOrders() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            OrderListState()
        )

    fun onAction(action: OrderListAction) {
        when (action) {
            is OrderListAction.onOrderClick -> {
                selectOrder(action.order)
            }
        }
    }

    private fun selectOrder(order: Order) {
        _state.update {
            it.copy(selectedOrder = order)
        }
    }


    private fun loadOrders() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            orderDataSource
                .getOrders()
        }
    }

}