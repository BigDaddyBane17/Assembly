package com.example.computershop.presentation.manager_screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computershop.data.database.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ManagerAuthenticationViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    fun logout() {
        _loginState.value = LoginState.Idle
    }

    fun loginManager(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val managerId = databaseHelper.readManager(username, password)
                if (managerId != -1) {
                    _loginState.value = LoginState.Success(managerId!!)
                } else {
                    _loginState.value = LoginState.Error("Неверные данные")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Ошибка в БД: ${e.message}")
            }
        }
    }


    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val userId: Int) : LoginState()
        data class Error(val message: String) : LoginState()
    }

}