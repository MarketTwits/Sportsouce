package com.markettwits.sportsouce.review.review.presentation.components.actual

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartCard

@Composable
fun ActualStarts(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (Int) -> Unit
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Актуальное",
        color = MaterialTheme.colorScheme.tertiary,
        fontFamily = FontNunito.bold(),
        fontSize = 18.sp
    )
    val isPortrait = rememberScreenSizeInfo().isPortrait()
    val items = starts.take(if (isPortrait) 5 else 10)
    FlowRow(
        modifier = modifier,
        maxItemsInEachRow = if (isPortrait) 1 else 2
    ) {
        items.forEach { item ->
            StartCard(
                modifier = Modifier.weight(1f),
                start = item, onItemClick = {
                    onClick(it)
                })
        }
    }
}