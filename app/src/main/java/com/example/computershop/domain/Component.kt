package com.example.computershop.domain

data class Component(
    val id : Int,
    val type : String,
    val name : String,
    val price : Double,
    val imageUri : String,
    val idOrder: Int? = null,
)
