package com.example.computershop.presentation.manager_screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.computershop.data.database.DatabaseHelper
import com.example.computershop.domain.Order
import com.example.computershop.presentation.client_screens.OrderListState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import java.text.NumberFormat

fun getStatusText(status: Int): String {
    return when (status) {
        1 -> "Новый заказ"
        2 -> "В процессе"
        3 -> "Готов"
        else -> "Неизвестный статус"
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ManagerOrderInfoScreen(
    state: ManagerOrderListState,
    modifier: Modifier = Modifier,
    databaseHelper: DatabaseHelper,
    onEditStatusClick: (Order) -> Unit // Лямбда для перехода на экран редактирования статуса
) {
    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Text("Загрузка...")
        }
    } else if (state.selectedOrder != null) {
        val order = state.selectedOrder

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {

            val pagerState = rememberPagerState()
            val componentImages = order.components.map { it.imageUri }
            Log.d("OrderInfoScreen", "componentImages: $componentImages")

            if (componentImages.isNotEmpty()) {
                HorizontalPager(
                    count = componentImages.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) { page ->
                    AsyncImage(
                        model = componentImages[page],
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                fontSize = 40.sp,
                text = order.name,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                fontSize = 18.sp,
                text = getStatusText(order.idStatus),
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                fontSize = 20.sp,
                text = "Цена: ${NumberFormat.getCurrencyInstance().format(order.totalPrice)}",
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            val client = remember { databaseHelper.getClientInfo(order.idClient) }
            Text(
                fontSize = 20.sp,
                text = "Информация о клиенте:",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            Text(
                fontSize = 18.sp,
                text = "Имя: ${client?.name ?: "Не найдено"}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                color = Color.Black
            )

            Text(
                fontSize = 18.sp,
                text = "Email: ${client?.email ?: "Не найдено"}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                color = Color.Black
            )

            Button(
                onClick = { onEditStatusClick(order) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Изменить статус")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                fontSize = 24.sp,
                text = "Описание заказа:",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(Modifier.height(16.dp))

            Text(
                fontSize = 18.sp,
                text = order.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}


@Composable
fun EditOrderStatusScreen(
    order: Order,
    databaseHelper: DatabaseHelper,
    onSave: (Int) -> Unit,
    onCancel: () -> Unit
) {
    var selectedStatus by remember { mutableStateOf(order.idStatus) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val statuses = listOf(1, 2, 3)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(WindowInsets.safeDrawing.asPaddingValues()), // Учитываем системные плашки
            verticalArrangement = Arrangement.SpaceBetween, // Распределение элементов
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Редактировать статус заказа",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { isMenuExpanded = true }) {
                    Text(text = "Выберите статус")
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    statuses.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(text = when (status) {
                                1 -> "Новый заказ"
                                2 -> "В процессе"
                                3 -> "Готов"
                                else -> "Неизвестный статус"
                            }) },
                            onClick = {
                                selectedStatus = status
                                isMenuExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Текущий статус: ${when (selectedStatus) {
                        1 -> "Новый заказ"
                        2 -> "В процессе"
                        3 -> "Готов"
                        else -> "Неизвестный статус"
                    }}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        databaseHelper.updateOrderStatus(order.id, selectedStatus)
                        onSave(selectedStatus)
                    },
                    modifier = Modifier.weight(1f) // Равномерное распределение ширины кнопок
                ) {
                    Text("Сохранить")
                }

                Button(
                    onClick = { onCancel() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Отмена")
                }
            }
        }
    }
}

