package com.markettwits.sportsouce.starts.favorites.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.sportsouce.starts.common.domain.StartsListItem


@Composable
internal fun StartsFavoritesContent(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    deletingStartIds: Set<Int>,
    removedStartIds: Set<Int>,
    isEmpty: Boolean,
    isLoading: Boolean,
    isRefreshing: Boolean = false,
    onClick: (StartsListItem) -> Unit,
    onFavoriteClick: (StartsListItem) -> Unit,
    onAddToFavorites: (StartsListItem) -> Unit,
    onRefresh: () -> Unit,
) {
    val screenSize = rememberScreenSizeInfo()
    val preferredWidth = 180f
    val columns = ((screenSize.wDP.value / preferredWidth).toInt())
        .coerceAtLeast(1)
        .coerceAtMost(4)

    AdaptivePane {
        PullToRefreshScreen(
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ) {
            if (isEmpty && !isLoading) {
                Box(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                ) {
                    FavoritesEmptyCard(
                        modifier = modifier
                            .padding(16.dp)
                    )
                }
            }
            if (items.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Show total count
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "Всего ${items.size} избранных стартов",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }

                    items(
                        items = items,
                        key = { it.id }
                    ) { item ->
                        FavoriteStartCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(
                                    fadeInSpec = tween(400),
                                    fadeOutSpec = tween(300),
                                    placementSpec = tween(400)
                                ),
                            start = item,
                            isDeleting = deletingStartIds.contains(item.id),
                            isRemoved = removedStartIds.contains(item.id),
                            onItemClick = onClick,
                            onFavoriteClick = onFavoriteClick,
                            onAddToFavorites = onAddToFavorites
                        )
                    }

                    // Bottom spacing
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
