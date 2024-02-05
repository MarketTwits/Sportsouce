package com.markettwits.start.presentation.order.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor

@Composable
fun StartDistanceInfoBox(
    modifier: Modifier = Modifier,
    format: String,
    distances: List<String>,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Text(
            modifier = modifier,
            text = "Старт",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.LightGray
        )
        Text(
            modifier = modifier,
            text = "Формат : $format",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.LightGray
        )
        Text(
            modifier = modifier,
            text = "Дистанция :",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.LightGray
        )
        LazyRow(modifier = modifier) {
            items(distances) {
                DistanceBox(value = it)
            }
        }
    }
}

@Composable
private fun DistanceBox(modifier: Modifier = Modifier, value: String) {
    Card(
        modifier = modifier,
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = SportSouceColor.SportSouceLighBlue)
    ) {
        Text(
            modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            text = value,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}