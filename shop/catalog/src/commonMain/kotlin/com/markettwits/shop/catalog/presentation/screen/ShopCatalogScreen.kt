package com.markettwits.shop.catalog.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.shop.cart.presentation.catalog.components.ShopCartCatalogWidget
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.catalog.presentation.components.ShopItemsContent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.shop.filter.presentation.components.api.SelectedFilterParams
import com.markettwits.shop.filter.presentation.screen.ShopFilterScreen
import com.markettwits.shop.search.presentation.components.publish.ShopSearchBar


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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

    var isShowFilter by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)

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
                    isShowFilter = !isShowFilter
                    // catalogComponent.obtainEvent(ShopCatalogStore.Intent.OnClickFilter)
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
        LaunchedEffect(isShowFilter) {
            if (isShowFilter) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
        ModalNavigationDrawer(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding()),
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ShopFilterScreen(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = 20.dp, topEnd = 20.dp))
                        .width(400.dp)
                        .fillMaxHeight(),
                    component = filterComponent,
                    isCompactMode = false
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
