package com.example.computershop.presentation.manager_screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.computershop.R
import com.example.computershop.domain.Order



fun getStatusText(status: Int): String {
    return when (status) {
        1 -> "Новый заказ"
        2 -> "В процессе"
        3 -> "Готов"
        else -> "Неизвестный статус"
    }
}

@Composable
fun ManagerOrder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    order: Order
) {


    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = order.components[0].imageUri,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp, 120.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = order.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
            )
            Text(
                text = "${order.totalPrice.toInt()} ₽" ,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_light))
            )


            Text(
                text = "Статус: ${getStatusText(order.idStatus)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.montserrat_light))
            )

        }

    }
}




//@Preview
//@Composable
//fun ManagerOrderCardPreview() {
//    ManagerOrder(order = previewOrder, onClick = {},  modifier = Modifier.background(
//        MaterialTheme.colorScheme.background
//    ))
//}
//
//
//internal val previewOrder = Order (
//    id = 1,
//    name = "Игровой ПК",
//    description = "Описание заказа",
//    totalPrice = 150000.0,
//    imageUri = "https://avatars.mds.yandex.net/i?id=a23509017f4ce7353d5cd6a15c470ec129915a3d-5233852-images-thumbs&n=13",
//    idStatus = 1,
//    idClient = 1,
//    idEmployee = 1,
//    components = emptyList()
//)