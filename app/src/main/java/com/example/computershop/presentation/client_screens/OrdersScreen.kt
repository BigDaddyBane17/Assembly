package com.example.computershop.presentation.client_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.computershop.R
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.domain.Order
import com.example.computershop.presentation.client_screens.components.OrderCard


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OrdersScreen(
    databaseHelper: DatabaseHelper,
    navController: NavController,
    idClient: Int,
    onAddOrder: () -> Unit,
    authenticationViewModel: AuthenticationViewModel,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val orders = remember { mutableStateOf(emptyList<Order>()) }

    // Загружаем заказы
    LaunchedEffect(Unit) {
        orders.value = databaseHelper.getOrdersByClient(idClient)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddOrder,
                modifier = Modifier.padding(16.dp),
                containerColor = Color(0xFFFF9800),
                contentColor = Color(0xFFFFFFFF)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить заказ")
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Мои заказы",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            showLogoutDialog = true
                        }
                    ) {
                        Icon(Icons.Default.ExitToApp, "ExitAccountButton")
                    }
                },
            )
        }
    ) { paddingValues ->
        if (orders.value.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет заказов",
                    fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(orders.value, key = { it.id }) { order ->
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                                databaseHelper.deleteOrderById(order.id)
                                orders.value = orders.value.filterNot { it.id == order.id }
                                true
                            } else false
                        }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        background = { SwipeBackground(dismissState) },
                        dismissContent = {
                            OrderCard(
                                order = order,
                                onClick = {
                                    navController.navigate("orderInfo/${order.id}",
                                        navOptions = navOptions {
                                            launchSingleTop = true
                                        })
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
            }
        }
    }

    // Логика выхода из аккаунта
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(
                text = "Выход из аккаунта",
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            ) },
            text = { Text(
                text = "Вы уверены, что хотите выйти из аккаунта?",
                fontFamily = FontFamily(Font(R.font.montserrat_light))
            ) },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    navController.navigate("login") {
                        authenticationViewModel.logout()
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Text(
                        text = "Выйти",
                        fontFamily = FontFamily(Font(R.font.montserrat_light))
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(
                        text = "Отмена",
                        fontFamily = FontFamily(Font(R.font.montserrat_light))
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Color.Red
        DismissDirection.EndToStart -> Color.Red
        null -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete Icon",
            tint = Color.White
        )
    }
}




