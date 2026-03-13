package com.markettwits.sportsouce.review.review.presentation.components.archive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.starts.common.domain.StartsListItem


@Composable
fun ArchiveStarts(
    modifier: Modifier = Modifier, starts: List<StartsListItem>,
    onClick: (StartsListItem) -> Unit,
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Архив",
        color = MaterialTheme.colorScheme.tertiary,
        fontFamily = FontNunito.bold(),
        fontSize = 18.sp
    )
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val cardWidth = (maxWidth * 0.18f).coerceIn(115.dp, 185.dp)

        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(starts, key = { it.id }) { item ->
                StartCardSimple(
                    cardWidth = cardWidth,
                    start = item,
                    onItemClick = { onClick(item) }
                )
            }
        }
    }
}
