package com.markettwits.sportsouce.review.review.presentation.components.actual

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartCardV3

@Composable
fun ActualStarts(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (StartsListItem) -> Unit,
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Актуальное",
        color = MaterialTheme.colorScheme.tertiary,
        fontFamily = FontNunito.bold(),
        fontSize = 18.sp
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .animateContentSize()
    ) {
        val horizontalGap = 12.dp
        val minCardWidth = 320.dp
        val columns = ((maxWidth + horizontalGap) / (minCardWidth + horizontalGap))
            .toInt()
            .coerceIn(1, 3)
        val items = starts.take(12)
        val cardWidth = (maxWidth - horizontalGap * (columns - 1)) / columns

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = columns,
            horizontalArrangement = Arrangement.spacedBy(horizontalGap),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                key(item.id) {
                    StartCardV3(
                        modifier = Modifier.width(cardWidth),
                        start = item,
                        onItemClick = { onClick(it) }
                    )
                }
            }
        }
    }
}
