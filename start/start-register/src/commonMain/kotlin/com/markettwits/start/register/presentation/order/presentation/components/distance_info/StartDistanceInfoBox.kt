package com.markettwits.start.register.presentation.order.presentation.components.distance_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.register.presentation.order.domain.OrderDistance

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartDistanceInfoBox(
    modifier: Modifier = Modifier,
    startTitle: String,
    distances: List<OrderDistance>,
    visibleIndex: Int,
    onClickDistance: (Int) -> Unit,
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
            value = distances[visibleIndex].format
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
        FlowRow(maxItemsInEachRow = 3) {
            distances.forEachIndexed { index, orderDistance ->

                SelectableDistanceBox(
                    modifier = Modifier.padding(4.dp),
                    value = orderDistance.distance,
                    isSelected = index == visibleIndex,
                    onClick = {
                        onClickDistance(index)
                    }
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

@Composable
private fun SelectableDistanceBox(
    modifier: Modifier,
    value: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(Shapes.medium)
            .clickable { onClick() }
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primary),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.secondary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                text = value,
                fontSize = 16.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}