package com.example.computershop.domain

data class Component(
    val id : Int,
    val type : String,
    val name : String,
    val price : Double,
    val imageUri : String,
    val socket : String? = null,
    val memoryType : String? = null,
    val idOrder: Int? = null,
)
