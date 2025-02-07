package com.markettwits.shop.catalog.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.presentation.toolbar.CollapsingToolbarScaffold
import com.markettwits.core_ui.items.presentation.toolbar.ScrollStrategy
import com.markettwits.core_ui.items.presentation.toolbar.rememberCollapsingToolbarScaffoldState
import com.markettwits.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.shop.cart.presentation.catalog.components.ShopCartCatalogWidget
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.shop.filter.presentation.components.api.SelectedFilterParams
import com.markettwits.shop.search.presentation.components.publish.ShopSearchBar


@Composable
fun ShopCatalogScreen(
    modifier: Modifier = Modifier,
    catalogComponent: ShopCatalogComponent,
    cartComponent: ShopCartCatalogComponent,
    filterComponent: ShopFilterComponent,
) {

    val state by catalogComponent.state.collectAsState()

   // val shopItems = state.shopItems.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ShopCartCatalogWidget(component = cartComponent)
        }
    ) { paddingValues ->
        CollapsingToolbarScaffold(
            modifier = modifier,
            state = rememberCollapsingToolbarScaffoldState(),
            scrollStrategy = ScrollStrategy.EnterAlways,
            toolbar = {
                ShopSearchBar(
                    query = state.queryState,
                    onClickSearchPanel = {
                        catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickSearch)
                    }, onClickFilter = {
                        catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickFilter)
                    },
                    onBrushClicked = {
                        catalogComponent.obtainEvent(ShopCatalogStore.Intent.ApplyQuery(""))
                    },
                    onClickBack = {
                        catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickGoBack)
                    }
                )
            }
        ) {
            Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                SelectedFilterParams(component = filterComponent)
//                ShopItemsContent(items = shopItems, onClickItem = {
//                    catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickItem(it))
//                })
            }
        }
    }
}
