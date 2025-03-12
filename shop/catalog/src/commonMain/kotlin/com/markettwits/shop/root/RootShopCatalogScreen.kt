package com.markettwits.shop.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.profile.api.root.RootAuthFlowScreen
import com.markettwits.shop.cart.presentation.cart.screen.ShopCartScreen
import com.markettwits.shop.catalog.presentation.screen.ShopCatalogScreen
import com.markettwits.shop.filter.presentation.screen.ShopFilterScreen
import com.markettwits.shop.item.presentation.screen.ShopItemPageScreen
import com.markettwits.shop.order.presentation.screen.ShopCreateOrderScreen
import com.markettwits.shop.orders.presentation.screen.ShopUserOrdersScreen
import com.markettwits.shop.search.presentation.screen.ShopSearchScreen

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
            is RootShopCatalogComponent.Child.ShopFilter -> ShopFilterScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopSearch -> ShopSearchScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopCart -> ShopCartScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopOrder -> ShopCreateOrderScreen(component = child.component)
            is RootShopCatalogComponent.Child.AuthFlow -> RootAuthFlowScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopUserOrders -> ShopUserOrdersScreen(component = child.component)
        }
    }
}