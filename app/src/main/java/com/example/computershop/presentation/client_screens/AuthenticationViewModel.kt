package com.example.computershop.presentation.client_screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computershop.data.database.DatabaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState


    var registrationState by mutableStateOf<RegistrationState>(RegistrationState.Idle)

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val userId = databaseHelper.readUser(username, password)
                if (userId != -1) {
                    _loginState.value = LoginState.Success(userId!!)
                } else {
                    _loginState.value = LoginState.Error("Неверные данные")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Ошибка в БД: ${e.message}")
            }
        }
    }

    fun registerUser(username: String, password: String, name: String, surname: String, phoneNumber: String, email: String) {
        viewModelScope.launch {
            registrationState = RegistrationState.Loading
            registrationState = try {
                val result = databaseHelper.insertUser(username, password, name, surname, phoneNumber, email)
                if (result > 0) {
                    RegistrationState.Success
                } else {
                    RegistrationState.Error("Неуспешная регистрация")
                }
            } catch (e: Exception) {
                RegistrationState.Error("Ошибка в БД: ${e.message}")
            }
        }
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val userId: Int) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    sealed class RegistrationState {
        data object Idle : RegistrationState()
        data object Loading : RegistrationState()
        data object Success : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }
}