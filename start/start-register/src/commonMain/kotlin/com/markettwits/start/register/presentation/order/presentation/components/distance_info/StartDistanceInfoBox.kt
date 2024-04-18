package com.markettwits.start.register.presentation.order.presentation.components.distance_info

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun StartDistanceInfoBox(
    modifier: Modifier = Modifier,
    format: String,
    startTitle: String,
    distances: List<String>,
) {
    OnBackgroundCard(modifier = modifier) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Старт",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        DistanceBox(
            modifier = Modifier.padding(4.dp),
            value = startTitle
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Формат",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        DistanceBox(
            modifier = Modifier.padding(4.dp),
            value = format
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Дистанция",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        Row {
            distances.forEach {
                DistanceBox(
                    modifier = Modifier.padding(4.dp),
                    value = it
                )
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
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}