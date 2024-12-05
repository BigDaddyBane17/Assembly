package com.example.computershop.presentation.manager_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.computershop.R
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
        Text(
            text = "Вход для менеджера",
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )


        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(
                text = "Логин",
                fontFamily = FontFamily(Font(R.font.montserrat_light))
            ) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(
                text = "Пароль",
                fontFamily = FontFamily(Font(R.font.montserrat_light))
            ) },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(356.dp))

        if (error.isNotBlank()) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = {
            if (username.isBlank() || password.isBlank()) {
                error = "Заполните, все поля, пожалуйста"
            } else {
                managerAuthenticationViewModel.loginManager(username, password)
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF9800),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFFFCC80),
                disabledContentColor = Color.Gray
            ),
            shape = RoundedCornerShape(16.dp)
            ) {
            Text(
                text = "Войти   ",
                fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                fontSize = 18.sp
            )
        }

        when (loginState) {
            is ManagerAuthenticationViewModel.ManagerLoginState.Loading -> {
                CircularProgressIndicator()
            }
            is ManagerAuthenticationViewModel.ManagerLoginState.Success -> {
                val userId = (loginState as ManagerAuthenticationViewModel.ManagerLoginState.Success).userId
                LaunchedEffect(Unit) {
                    onLoginSuccess(userId)
                }
            }
            is ManagerAuthenticationViewModel.ManagerLoginState.Error -> {
                error = (loginState as ManagerAuthenticationViewModel.ManagerLoginState.Error).message
            }
            else -> {
                //smth
            }
        }
    }
}