package com.example.computershop.presentation.manager_screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.computershop.R
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.domain.Order
import com.example.computershop.presentation.manager_screens.components.ManagerOrder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerClientsOrdersScreen(
    databaseHelper: DatabaseHelper,
    navController: NavController,
    managerAuthenticationViewModel: ManagerAuthenticationViewModel,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current


    BackHandler {
        managerAuthenticationViewModel.logout()
        (context as? Activity)?.finish()
    }

    var showLogoutDialog by remember { mutableStateOf(false) }
    val orders = remember { mutableStateOf(emptyList<Order>()) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<Int?>(null) }
    var priceRange by remember { mutableStateOf(0..1000000) }
    var minPriceInput by remember { mutableStateOf(priceRange.first.toString()) }
    var maxPriceInput by remember { mutableStateOf(priceRange.last.toString()) }

    LaunchedEffect(searchQuery, selectedStatus, priceRange) {
        val allOrders = databaseHelper.getAllOrders()
        orders.value = allOrders.filter { order ->
            (searchQuery.isEmpty() || order.name.contains(
                searchQuery,
                ignoreCase = true
            ) || order.description.contains(searchQuery, ignoreCase = true)) &&
                    (selectedStatus == null || order.idStatus == selectedStatus) &&
                    (order.totalPrice.toInt() in priceRange.first..priceRange.last)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Заказы клиентов",
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
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            var isDropdownExpanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(
                    onClick = { isDropdownExpanded = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = selectedStatus?.let { "Статус: ${statusLabel(it)}" }
                            ?: "Выберите статус"
                    )
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Все") },
                        onClick = { selectedStatus = null }
                    )
                    DropdownMenuItem(
                        text = { Text("Новый заказ") },
                        onClick = { selectedStatus = 1 }
                    )
                    DropdownMenuItem(
                        text = { Text("В процессе") },
                        onClick = { selectedStatus = 2 }
                    )
                    DropdownMenuItem(
                        text = { Text("Готов") },
                        onClick = { selectedStatus = 3 }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = minPriceInput,
                    onValueChange = {
                        minPriceInput = it
                        priceRange = try {
                            val newMin = it.toIntOrNull() ?: 0
                            val newMax = maxPriceInput.toIntOrNull() ?: priceRange.last
                            newMin..newMax
                        } catch (e: Exception) {
                            priceRange
                        }
                    },
                    label = { Text("Мин. цена") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = maxPriceInput,
                    onValueChange = {
                        maxPriceInput = it
                        priceRange = try {
                            val newMax = it.toIntOrNull() ?: priceRange.last
                            val newMin = minPriceInput.toIntOrNull() ?: 0
                            newMin..newMax
                        } catch (e: Exception) {
                            priceRange
                        }
                    },
                    label = { Text("Макс. цена") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }


            if (orders.value.isEmpty()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
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
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(orders.value) { order ->
                        ManagerOrder(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate("managerOrderInfo/${order.id}",
                                    navOptions = navOptions {
                                        launchSingleTop = true
                                    })
                            },
                            order = order
                        )
                    }
                }
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = "Выход из аккаунта",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                },
                text = {
                    Text(
                        text = "Вы уверены, что хотите выйти из аккаунта?",
                        fontFamily = FontFamily(Font(R.font.montserrat_light))
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        navController.navigate("login") {
                            managerAuthenticationViewModel.logout()
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

}


@Composable
fun statusLabel(status: Int): String = when (status) {
    1 -> "Новый заказ"
    2 -> "В процессе"
    3 -> "Готов"
    else -> "Неизвестно"
}