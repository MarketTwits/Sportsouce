package com.markettwits.sportsouce.review.review.presentation.components.actual

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
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
    val screenWidth = rememberScreenSizeInfo().wDP
    val isTwoColumns = screenWidth >= 680.dp
    val items = starts.take(if (isTwoColumns) 10 else 5)

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val horizontalGap = 8.dp
        val cardWidth = if (isTwoColumns) {
            (maxWidth - horizontalGap) / 2
        } else {
            maxWidth
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = if (isTwoColumns) 2 else 1,
            horizontalArrangement = Arrangement.spacedBy(horizontalGap),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEach { item ->
                StartCardV3(
                    modifier = Modifier.width(cardWidth),
                    start = item,
                    onItemClick = { onClick(it) }
                )
            }
        }
    }
}
