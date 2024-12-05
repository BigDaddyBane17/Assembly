package com.example.computershop.presentation.client_screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.computershop.R

@OptIn(ExperimentalMaterial3Api::class)
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



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("managerLogin",
                            navOptions = navOptions {
                                launchSingleTop = true
                            }) } // Переход к экрану менеджера
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.management), // Подключение иконки
                            contentDescription = "Менеджер",
                            tint = Color.Unspecified
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Вход в аккаунт",
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(
                    text = "Логин",
                    fontFamily = FontFamily(Font(R.font.montserrat_light))
                ) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(16.dp))

            if (error.isNotBlank()) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }


            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Нет аккаунта? Зарегистрироваться",
                fontFamily = FontFamily(Font(R.font.montserrat_light)),
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    navController.navigate(
                        "register",
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                }
            )

            Spacer(Modifier.height(256.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = "Пожалуйста заполните все поля"
                } else {
                    authenticationViewModel.loginUser(email, password)
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
                    text = "Войти",
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                    fontSize = 18.sp
                )
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


}


