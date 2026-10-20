package com.markettwits.sportsouce.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid as LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.shop.domain.model.ShopItem

@Composable
fun ShopItemsBase(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    items: LazyPagingItems<ShopItem>,
    onClickItem: (ShopItem) -> Unit,
) {
    val backgroundColor = SauceTheme.colors.background

    PullToRefreshScreen(
        isRefreshing = isRefreshing,
        onRefresh = items::refresh
    ) {
    BoxWithConstraints(modifier) {
        val minimumWidth = 160.dp * LocalDensity.current.fontScale.coerceAtMost(1.5f)
        val columns = ((maxWidth - 32.dp) / minimumWidth).toInt().coerceIn(1, 5)
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items.itemCount,
                key = items.itemKey { it.id }
            ) { index ->
                items[index]?.let { shopItem ->
                    ShopItemCard(
                        modifier = Modifier,
                        shopItem = shopItem,
                        onItemClick = onClickItem,
                    )
                }
            }
            if (items.loadState.append is LoadStateLoading) {
                items(20) {
                    ShopItemShimmer()
                }
            }
        }
    }
    }
}
