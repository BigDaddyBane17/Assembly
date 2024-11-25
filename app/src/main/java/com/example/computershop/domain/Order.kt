package com.example.computershop.domain

data class Order(
    val id: Int,
    val name: String,
    val description: String,
    val totalPrice: Double,
    val imageUri: String?,
    val idEmployee : Int,
    val idClient: Int,
    val idStatus: Int,
    val components: List<Component>
)