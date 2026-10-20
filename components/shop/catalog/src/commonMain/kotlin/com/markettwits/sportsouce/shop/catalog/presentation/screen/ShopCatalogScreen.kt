package com.markettwits.sportsouce.shop.catalog.presentation.screen

import com.markettwits.core_ui.items.components.topbar.SauceTopBarSection
import com.markettwits.core_ui.items.components.topbar.SauceScaffold as Scaffold
import com.markettwits.core_ui.items.theme.SauceTheme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.screens.AdaptivePane
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

    val backgroundColor = SauceTheme.colors.background

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()
    var wasDrawerOpen by remember { mutableStateOf(false) }
    // Drawer gestures and Back must update the same visibility flag as the toolbar action.
    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.currentValue == DrawerValue.Open) {
            wasDrawerOpen = true
            if (!state.isShowFilter) catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickFilter)
        }
        else if (wasDrawerOpen) {
            wasDrawerOpen = false
            if (state.isShowFilter) catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickFilter)
        }
    }

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
                    onClickGoBack = {
                        catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickFilter)
                    },
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
                        SauceTopBarSection {
                            SelectedFilterParams(component = filterComponent)
                        }

                        ShopItemsContent(items = shopItems, onClickItem = {
                            catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickItem(it))
                        })
                    }
                }
            }
        )
    }
}
