package com.example.computershop.presentation.manager_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.computershop.presentation.client_screens.AuthenticationViewModel
@Composable
fun ManagerLoginScreen(
    managerAuthenticationViewModel: ManagerAuthenticationViewModel,
    onLoginSuccess: (Int) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val loginState by managerAuthenticationViewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (error.isNotBlank()) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            if (username.isBlank() || password.isBlank()) {
                error = "Заполните, все поля, пожалуйста"
            } else {
                managerAuthenticationViewModel.loginManager(username, password)
            }
        }) {
            Text("Войти как менеджер")
        }

        when (loginState) {
            is ManagerAuthenticationViewModel.LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is ManagerAuthenticationViewModel.LoginState.Success -> {
                val userId = (loginState as ManagerAuthenticationViewModel.LoginState.Success).userId
                LaunchedEffect(Unit) {
                    onLoginSuccess(userId)
                }
            }
            is ManagerAuthenticationViewModel.LoginState.Error -> {
                error = (loginState as AuthenticationViewModel.LoginState.Error).message
            }
            else -> {
                //smth
            }
        }
    }
}