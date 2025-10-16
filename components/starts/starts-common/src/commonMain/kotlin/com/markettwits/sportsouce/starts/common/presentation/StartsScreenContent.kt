package com.markettwits.sportsouce.starts.common.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
fun StartsScreenContent(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    onClick: (StartsListItem) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(380.dp)
    ) {
        items(items) {
            StartCardV2(
                modifier = Modifier.animateItem(fadeInSpec = tween(600)),
                start = it,
                onItemClick = { startId: StartsListItem ->
                    onClick(startId)
                }
            )
        }
    }
}