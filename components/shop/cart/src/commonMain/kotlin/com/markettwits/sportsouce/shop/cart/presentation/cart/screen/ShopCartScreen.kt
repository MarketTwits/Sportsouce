package com.markettwits.sportsouce.shop.cart.presentation.cart.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.sportsouce.shop.cart.presentation.cart.components.CreateOrderButton
import com.markettwits.sportsouce.shop.cart.presentation.cart.components.ShopCartItems

@Composable
fun ShopCartScreen(
    modifier: Modifier = Modifier,
    component: ShopCartComponent
) {

    val state by component.state.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarBase(title = "Корзина", goBack = component::onClickGoBack)
        }
    ) { paddingValues ->
        AdaptivePane {
            LazyColumn(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
            ) {
                ShopCartItems(
                    items = state.items,
                    onClickDecrease = component::onClickDecrease,
                    onClickIncrease = component::onClickIncrease,
                    onClickItem = component::onClickShopCartItem,
                    onClickDelete = component::onClickDelete
                )
                item{
                    CreateOrderButton(
                        onClickCreateOrder = component::onClickCreateOrder,
                        onClickGoAuth = component::onClickGoAuth,
                        isAvailable = state.isAvailable
                    )
                }
            }
        }
    }
}