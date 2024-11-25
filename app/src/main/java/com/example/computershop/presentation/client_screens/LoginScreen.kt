package com.example.computershop.presentation.client_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel,
    onLoginSuccess: (Int) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val showPassword by remember { mutableStateOf(false) }

    val loginState by authenticationViewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Имя") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        )

        Spacer(modifier = Modifier.height(16.dp))

        if (error.isNotBlank()) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = {
            if (email.isBlank() || password.isBlank()) {
                error = "Пожалуйста заполните все поля"
            } else {
                authenticationViewModel.loginUser(email, password)
            }
        }) {
            Text("Войти")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("register") }) {
            Text("Нет аккаунта? Зарегистрироваться")
        }

        Spacer(modifier = Modifier.height(312.dp))

        Button(
            onClick = { navController.navigate("managerLogin") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Войти как менеджер")
        }

        when (loginState) {
            is AuthenticationViewModel.LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthenticationViewModel.LoginState.Success -> {
                val userId = (loginState as AuthenticationViewModel.LoginState.Success).userId
                LaunchedEffect(Unit) {
                    onLoginSuccess(userId)
                }
            }
            is AuthenticationViewModel.LoginState.Error -> {
                error = (loginState as AuthenticationViewModel.LoginState.Error).message
            }
            else -> {
                //smth
            }
        }
    }
}


