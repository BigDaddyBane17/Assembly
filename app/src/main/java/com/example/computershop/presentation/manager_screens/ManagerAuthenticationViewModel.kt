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

    private val _loginState = MutableStateFlow<ManagerLoginState>(ManagerLoginState.Idle)
    val loginState: StateFlow<ManagerLoginState> get() = _loginState

    fun logout() {
        _loginState.value = ManagerLoginState.Idle
    }

    fun loginManager(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ManagerLoginState.Loading
            try {
                val managerId = databaseHelper.readManager(username, password)
                if (managerId != -1) {
                    _loginState.value = ManagerLoginState.Success(managerId!!)
                } else {
                    _loginState.value = ManagerLoginState.Error("Неверные данные")
                }
            } catch (e: Exception) {
                _loginState.value = ManagerLoginState.Error("Ошибка в БД: ${e.message}")
            }
        }
    }


    sealed class ManagerLoginState {
        data object Idle : ManagerLoginState()
        data object Loading : ManagerLoginState()
        data class Success(val userId: Int) : ManagerLoginState()
        data class Error(val message: String) : ManagerLoginState()
    }

}