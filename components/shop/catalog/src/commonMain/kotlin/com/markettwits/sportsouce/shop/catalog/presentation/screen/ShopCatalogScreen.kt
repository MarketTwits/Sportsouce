package com.markettwits.sportsouce.shop.catalog.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.sportsouce.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.sportsouce.shop.cart.presentation.catalog.components.ShopCartCatalogWidget
import com.markettwits.sportsouce.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.sportsouce.shop.catalog.presentation.components.ShopItemsContent
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.sportsouce.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.sportsouce.shop.filter.presentation.components.api.SelectedFilterParams
import com.markettwits.sportsouce.shop.filter.presentation.screen.ShopFilterScreen
import com.markettwits.sportsouce.shop.search.presentation.components.publish.ShopSearchBar
import kotlinx.coroutines.launch

@Composable
fun ShopCatalogScreen(
    modifier: Modifier = Modifier,
    catalogComponent: ShopCatalogComponent,
    cartComponent: ShopCartCatalogComponent,
    filterComponent: ShopFilterComponent,
) {

    val state by catalogComponent.state.collectAsState()

    val shopItems = state.shopItems.collectAsLazyPagingItems()

    val backgroundColor =
        if (LocalDarkOrLightTheme.current) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.outlineVariant

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.End,
        containerColor = backgroundColor,
        floatingActionButton = {
            AnimatedVisibility(!drawerState.isOpen) {
                ShopCartCatalogWidget(component = cartComponent)
            }
        }, topBar = {
            ShopSearchBar(
                query = state.queryState,
                isShowFilter = true,
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
    ) { paddingValues ->
        LaunchedEffect(state) {
            if (state.isShowFilter) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
        ModalNavigationDrawer(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
            drawerState = drawerState,
            drawerContent = {
                ShopFilterScreen(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = 20.dp, topEnd = 20.dp))
                        .width(400.dp)
                        .fillMaxHeight(),
                    component = filterComponent,
                    onClickApplyFilter = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            content = {
                AdaptivePane(
                    backgroundColor = backgroundColor
                ) {
                    Column {
                        SelectedFilterParams(component = filterComponent)
                        ShopItemsContent(items = shopItems, onClickItem = {
                            catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickItem(it))
                        })
                    }
                }
            }
        )
    }
}
