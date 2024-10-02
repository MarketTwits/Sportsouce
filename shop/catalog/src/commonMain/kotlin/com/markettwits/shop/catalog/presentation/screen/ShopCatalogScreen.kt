package com.markettwits.shop.catalog.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.catalog.presentation.components.ShopItemCard
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.presentation.components.api.ShopCategories
import com.markettwits.shop.paging.fold
import com.markettwits.shop.root.RootShopCatalogComponent
import com.markettwits.shop.search.presentation.components.publish.ShopSearchBar
import kotlinx.collections.immutable.immutableListOf


@Composable
fun ShopCatalogScreen(
    modifier: Modifier = Modifier,
    component: ShopCatalogComponent,
) {

    val state by component.state.collectAsState()

    val backgroundColor =
        if (LocalDarkOrLightTheme.current) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant

    val itemsState = state.shopItems.collectAsLazyPagingItems()

    Scaffold(modifier = modifier, topBar = {
        ShopSearchBar(
            query = state.queryState,
            onClickSearchPanel = {
                component.obtainEvent(ShopCatalogStore.Intent.OnClickSearch)
            }, onClickFilter = {
                component.obtainEvent(ShopCatalogStore.Intent.OnClickFilter)
            },
            onBrushClicked = {
                component.obtainEvent(ShopCatalogStore.Intent.ApplyQuery(""))
            },
            onClickBack = {
                component.obtainEvent(ShopCatalogStore.Intent.OnClickGoBack)
            }
        )
    }) { paddingValues ->
        Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            state.filterState?.let {
                ShopCategories(
                    selectedCategoriesPath = state.filterState?.currentCategoryPath ?: emptyList(),
                    onClickCategory = {
                        component.obtainEvent(ShopCatalogStore.Intent.OnClickCategory(it))
                    }
                )
            }
            itemsState.fold(onLoading = {
                LoadingFullScreen()
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
                                        component.obtainEvent(
                                            ShopCatalogStore.Intent.OnClickItem(
                                                id
                                            )
                                        )
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
}
