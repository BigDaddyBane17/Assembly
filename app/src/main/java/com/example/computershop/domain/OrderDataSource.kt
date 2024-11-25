package com.example.computershop.domain

interface OrderDataSource {
    suspend fun getOrders() : List<Order>
}