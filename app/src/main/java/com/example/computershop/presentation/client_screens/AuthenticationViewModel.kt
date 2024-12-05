package com.example.computershop.presentation.client_screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.presentation.manager_screens.ManagerAuthenticationViewModel.ManagerLoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState


    var registrationState by mutableStateOf<RegistrationState>(RegistrationState.Idle)

    fun loginUser(login: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val userId = databaseHelper.readUser(login, password)
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

    fun registerUser(login: String, password: String, name: String, surname: String, phoneNumber: String, email: String) {
        viewModelScope.launch {
            registrationState = RegistrationState.Loading
            try {
                if (databaseHelper.isUsernameTaken(login)) {
                    registrationState = RegistrationState.Error("Логин уже занят другим пользователем")
                    return@launch
                }

                val result = databaseHelper.insertUser(login, password, name, surname, phoneNumber, email)
                if (result > 0) {
                    registrationState = RegistrationState.Success
                } else {
                    registrationState = RegistrationState.Error("Неуспешная регистрация")
                }
            } catch (e: Exception) {
                registrationState = RegistrationState.Error("Ошибка в БД: ${e.message}")
            }
        }
    }


    fun logout() {
        _loginState.value = LoginState.Idle
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