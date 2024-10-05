package com.markettwits.shop.catalog.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.shop.cart.presentation.catalog.components.ShopCartCatalogWidget
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.catalog.presentation.components.ShopItemsContent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.presentation.components.api.ShopCategories
import com.markettwits.shop.search.presentation.components.publish.ShopSearchBar


@Composable
fun ShopCatalogScreen(
    modifier: Modifier = Modifier,
    component: ShopCatalogComponent,
    cartComponent : ShopCartCatalogComponent
) {

    val state by component.state.collectAsState()

    val shopItems = state.shopItems.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier,
        topBar = {
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
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ShopCartCatalogWidget(component = cartComponent)
        }
        ) { paddingValues ->
        Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
            ShopCategories(
                selectedCategoriesPath = state.filterState?.currentCategoryPath ?: emptyList(),
                onClickCategory = {

                })
            ShopItemsContent(items = shopItems, onClickItem = {
                component.obtainEvent(ShopCatalogStore.Intent.OnClickItem(it))
            })
        }
    }
}
