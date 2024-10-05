package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.shop.catalog.domain.models.ShopItem
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.presentation.components.api.ShopCategories
import com.markettwits.shop.paging.fold


@Composable
fun ShopItemsContent(
    items: LazyPagingItems<ShopItem>,
    onClickItem: (ShopItem) -> Unit

) {
        items.fold(onLoading = {
            LoadingFullScreen()
        }, onException = {
            FailedScreen(message = (it.message.toString()), onClickRetry = items::refresh)
        }, onSuccess = { isRefreshing ->
            ShopItemsBase(isRefreshing = isRefreshing, items = items, onClickItem = onClickItem)
        },
            onEmpty = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "По вашему запросу не были найдены данные"
                    )
                }
            }
        )
}