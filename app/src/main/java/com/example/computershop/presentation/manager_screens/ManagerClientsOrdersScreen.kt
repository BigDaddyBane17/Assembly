package com.example.computershop.presentation.manager_screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.domain.Order
import com.example.computershop.presentation.manager_screens.components.ManagerOrder


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

    val orders = remember { mutableStateOf(emptyList<Order>()) }

    LaunchedEffect(Unit) {
        orders.value = databaseHelper.getAllOrders()
    }

    Scaffold(
    ) { paddingValues ->
        if (orders.value.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет заказов")
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orders.value) { order ->
                    ManagerOrder(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate("managerOrderInfo/${order.id}")
                        },
                        order = order
                    )
                }
            }
        }
    }
}