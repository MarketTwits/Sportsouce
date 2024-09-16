package com.markettwits.shop.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.shop.catalog.presentation.component.CardsComponent
import com.markettwits.shop.item.presentation.component.ShopItemPageComponent
import kotlinx.serialization.Serializable

interface RootShopCatalogComponent {

    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed interface Config {

        @Serializable
        data object ShopCatalog : Config

        @Serializable
        data class ShopItem(val id: String) : Config

    }

    sealed interface Child {

        data class ShopCatalog(val component: CardsComponent) : Child

        data class ShopItem(val component: ShopItemPageComponent) : Child
    }

}