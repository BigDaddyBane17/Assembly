package com.example.computershop.di

import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.presentation.client_screens.AuthenticationViewModel
import com.example.computershop.presentation.client_screens.OrderListViewModel
import com.example.computershop.presentation.manager_screens.ManagerAuthenticationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    viewModelOf(::OrderListViewModel)
    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::ManagerAuthenticationViewModel)
    single { DatabaseHelper(get()) }
}
