package com.markettwits.sportsouce.starts.favorites.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid as LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import androidx.compose.ui.platform.LocalDensity
import com.markettwits.core_ui.items.theme.SauceTheme
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
                    columns = GridCells.Adaptive(minSize = 320.dp * LocalDensity.current.fontScale),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Show total count
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            modifier = Modifier.padding(bottom = 4.dp),
                            text = "В избранном: ${items.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SauceTheme.colors.textSecondary
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
