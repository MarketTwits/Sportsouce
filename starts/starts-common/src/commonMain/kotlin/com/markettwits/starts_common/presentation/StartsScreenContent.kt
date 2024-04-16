package com.markettwits.starts_common.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.starts_common.domain.StartsListItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartsScreenContent(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    onClick: (Int) -> Unit
) {
    LazyColumn(modifier) {
        items(items, key = { it -> it.id }) {
            StartCard(
                modifier = Modifier.animateItemPlacement(animationSpec = tween(600)),
                start = it,
                onItemClick = { startId ->
                    onClick(startId)
                }
            )
        }
    }
}