package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.shop.domain.model.ShopItem

@Composable
fun ShopItemsBase(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    items: LazyPagingItems<ShopItem>,
    onClickItem: (ShopItem) -> Unit,
) {
    val backgroundColor =
        if (LocalDarkOrLightTheme.current) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant

    PullToRefreshScreen(
        isRefreshing = isRefreshing,
        onRefresh = items::refresh
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor),
            columns = GridCells.Fixed(2),
        ) {
            items(
                count = items.itemCount,
                key = items.itemKey { it.id },
            ) { index ->
                items[index]?.let {
                    ShopItemCard(shopItem = it, onItemClick = onClickItem)
                }
            }

        }
    }
}
