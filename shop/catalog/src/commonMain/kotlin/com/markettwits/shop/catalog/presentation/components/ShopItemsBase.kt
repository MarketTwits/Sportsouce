package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.shop.domain.model.ShopItem
import kotlin.math.min

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
        val screenWidth = rememberScreenSizeInfo().wDP
        val maxColumns = 5
        val columnWidth = 200.dp
        val columns = min(maxColumns, (screenWidth / columnWidth).toInt())
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor),
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(
                items.itemCount,
                key = items.itemKey { it.id }
            ) { index ->
                items[index]?.let { shopItem ->
                    ShopItemCard(
                        modifier = Modifier.width(columnWidth),
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