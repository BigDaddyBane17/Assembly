package com.example.computershop.presentation.client_screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import java.text.NumberFormat
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.HorizontalPagerIndicator

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
fun OrderInfoScreen(
    state: OrderListState,
    modifier: Modifier = Modifier
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
                text = "Цена: ${NumberFormat.getCurrencyInstance().format(order.totalPrice)}", // Форматирование цены

                textAlign = TextAlign.Center,
                color = Color.Black
            )

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




//
//@Preview
//@Composable
//fun OrderInfoScreenPreview(modifier: Modifier = Modifier) {
//    OrderInfoScreen(
//        state = OrderListState(
//            selectedOrder = previewOrder
//        ),
//        modifier = Modifier
//            .background(
//                MaterialTheme.colorScheme.background
//            )
//    )
//}