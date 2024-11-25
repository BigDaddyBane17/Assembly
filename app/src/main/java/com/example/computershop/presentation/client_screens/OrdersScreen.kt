package com.example.computershop.presentation.client_screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.domain.Component
import com.example.computershop.domain.Order
import com.example.computershop.presentation.client_screens.components.OrderCard




@Composable
fun OrdersScreen(
    databaseHelper: DatabaseHelper,
    navController: NavController,
    idClient: Int,
    onAddOrder: () -> Unit,
    modifier: Modifier = Modifier
) {

    val orders = remember { mutableStateOf(emptyList<Order>()) }

    LaunchedEffect(Unit) {
        orders.value = databaseHelper.getOrdersByClient(idClient)
        Log.d("taaaaaaaag", "$idClient")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddOrder,
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить заказ")
            }
        }
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
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(orders.value) { order ->
                    OrderCard(
                        order = order,
                        onClick = {
                            navController.navigate("orderInfo/${order.id}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


