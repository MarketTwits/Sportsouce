package com.markettwits.shop.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.shop.catalog.presentation.components.ShopItemCardBaseList
import com.markettwits.shop.filter.presentation.screen.ShopFilterScreen
import com.markettwits.shop.item.presentation.screen.ShopItemPageScreen

@Composable
fun RootShopCatalogScreen(component: RootShopCatalogComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootShopCatalogComponent.Child.ShopCatalog -> ShopItemCardBaseList(component = child.component)
            is RootShopCatalogComponent.Child.ShopItem -> ShopItemPageScreen(component = child.component)
            is RootShopCatalogComponent.Child.ShopFilter -> ShopFilterScreen(component = child.component)
        }
    }
}