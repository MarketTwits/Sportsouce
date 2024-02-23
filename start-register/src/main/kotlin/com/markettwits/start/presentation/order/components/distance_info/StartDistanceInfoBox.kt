package com.markettwits.start.presentation.order.components.distance_info

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun StartDistanceInfoBox(
    modifier: Modifier = Modifier,
    format: String,
    distances: List<String>,
) {
    OnBackgroundCard {
        Text(
            modifier = modifier,
            text = "Старт",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            modifier = modifier,
            text = "Формат : $format",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            modifier = modifier,
            text = "Дистанция :",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        LazyRow(modifier = modifier) {
            items(distances) {
                DistanceBox(
                    value = it
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            }
        }
    }
}

@Composable
private fun DistanceBox(modifier: Modifier = Modifier, value: String) {
    Card(
        modifier = modifier,
        shape = Shapes.medium,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            text = value,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}