package com.markettwits.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.itemKey
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.base_screen.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.paging.fold


@Composable
fun ShopItemCardBaseList(
    modifier: Modifier = Modifier,
    component: ShopCatalogComponent,
) {

    val state by component.state.collectAsState()

    val backgroundColor =
        if (LocalDarkOrLightTheme.current) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant

    val itemsState = state.shopItems.collectAsLazyPagingItems()

    Column {
        ShopCategories(shopCategoryItem = state.categories,
            selectedCategoryItemId = state.options.catalogId ?: 0,
            onItemClick = {
                component.obtainEvent(ShopCatalogStore.Intent.OnClickCategory(it.id))
            })
        Button(onClick = {
            component.obtainEvent(ShopCatalogStore.Intent.OnClickFilter)
        }) {
            Text("Фильтр")
        }
        itemsState.fold(onLoading = {
            LoadingFullScreen { }
        }, onException = {
            FailedScreen(
                message = (it.message.toString()),
                onClickRetry = itemsState::refresh
            )
        }, onSuccess = { isRefreshing ->
            PullToRefreshScreen(
                isRefreshing = isRefreshing,
                onRefresh = itemsState::refresh
            ) {
                Column {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor),
                        columns = GridCells.Fixed(2),
                    ) {
                        items(
                            count = itemsState.itemCount,
                            key = itemsState.itemKey { it.id },
                        ) { index ->
                            itemsState[index]?.let {
                                ShopItemCard(shopItem = it, onItemClick = { id ->
                                    component.obtainEvent(ShopCatalogStore.Intent.OnClickItem(id))
                                })
                            }
                        }

                    }
                }
            }
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
}
