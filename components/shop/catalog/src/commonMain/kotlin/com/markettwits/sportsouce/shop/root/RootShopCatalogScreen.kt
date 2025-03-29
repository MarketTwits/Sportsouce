package com.markettwits.sportsouce.shop.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowScreen
import com.markettwits.sportsouce.shop.cart.presentation.cart.screen.ShopCartScreen
import com.markettwits.sportsouce.shop.catalog.presentation.screen.ShopCatalogScreen
import com.markettwits.sportsouce.shop.filter.presentation.screen.ShopFilterScreen
import com.markettwits.sportsouce.shop.item.presentation.screen.ShopItemPageScreen
import com.markettwits.sportsouce.shop.order.presentation.screen.ShopCreateOrderScreen
import com.markettwits.sportsouce.shop.orders.presentation.screen.ShopUserOrdersScreen
import com.markettwits.sportsouce.shop.search.presentation.screen.ShopSearchScreen

@Composable
fun RootShopCatalogScreen(component: RootShopCatalogComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootShopCatalogComponent.Child.ShopCatalog -> ShopCatalogScreen(
                catalogComponent = child.componentPage,
                cartComponent = child.componentCart,
                filterComponent = child.componentFilter
            )
            is RootShopCatalogComponent.Child.ShopItem -> ShopItemPageScreen(
                component = child.componentPage,
                cartComponent = child.componentCart
            )
            is RootShopCatalogComponent.Child.ShopFilter -> ShopFilterScreen(
                component = child.component,
                onClickApplyFilter = {}

            )
            is RootShopCatalogComponent.Child.ShopSearch -> ShopSearchScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopCart -> ShopCartScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopOrder -> ShopCreateOrderScreen(component = child.component)
            is RootShopCatalogComponent.Child.AuthFlow -> RootAuthFlowScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopUserOrders -> ShopUserOrdersScreen(component = child.component)
        }
    }
}