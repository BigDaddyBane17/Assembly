package com.example.computershop.presentation.client_screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.computershop.R
import com.example.computershop.domain.Order
import com.example.computershop.presentation.client_screens.getStatusText


@Composable
fun OrderCard(
    order: Order,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = order.components[0].imageUri,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp, 100.dp)
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
//fun OrderCardPreview() {
//    OrderCard(order = previewOrder, onClick = {},  modifier = Modifier.background(
//        MaterialTheme.colorScheme.background
//    ))
//}


