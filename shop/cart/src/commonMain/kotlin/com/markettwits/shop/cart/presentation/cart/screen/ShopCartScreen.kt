package com.markettwits.shop.cart.presentation.cart.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.top_bar.TopBarBase
import com.markettwits.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.shop.cart.presentation.cart.components.ShopCartItems
import com.markettwits.shop.cart.presentation.cart.components.ShopCartOrder

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
        LazyColumn(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            ShopCartItems(
                items = state.items,
                onClickDecrease = component::onClickDecrease,
                onClickIncrease = component::onClickIncrease,
                onClickItem = component::onClickShopCartItem
            )
            item{
                ShopCartOrder(
                    modifier = Modifier.padding(10.dp),
                    itemsCount = state.order.itemsCount,
                    discount = state.order.discount,
                    totalCost = state.order.totalCost,
                    isByCache = state.order.payByCache,
                    isCreateOrderAvailable = true,
                    onClickChangePaymentType = component::onClickChangePaymentType,
                    onClickCreateOrder = component::onClickCreateOrder
                )
            }
        }
    }

}