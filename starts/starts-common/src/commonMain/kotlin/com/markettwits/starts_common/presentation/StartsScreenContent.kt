package com.markettwits.starts_common.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.starts_common.domain.StartsListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartsScreenContent(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    isMaxWith: Boolean = false,
    onClick: (Int) -> Unit
) {
    if (isMaxWith) {
        val size = rememberScreenSizeInfo()
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(if (size.isPortrait()) 1 else 2)
        ) {
            items(items) {
                StartCard(
                    modifier = Modifier,
                    start = it,
                    onItemClick = { startId ->
                        onClick(startId)
                    }
                )
            }
        }
    } else {
        LazyColumn(modifier) {
            items(items, key = { it.id }) {
                StartCard(
                    modifier = Modifier.animateItem(fadeInSpec = tween(600)),
                    start = it,
                    onItemClick = { startId ->
                        onClick(startId)
                    }
                )
            }
        }
    }
}