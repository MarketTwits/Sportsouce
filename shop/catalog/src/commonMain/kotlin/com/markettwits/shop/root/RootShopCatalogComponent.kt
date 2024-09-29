package com.markettwits.shop.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
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

        @Serializable
        data class ShopFilter(val state: ShopFilterStore.State?) : Config

    }

    sealed interface Child {

        data class ShopCatalog(val component: ShopCatalogComponent) : Child

        data class ShopItem(val component: ShopItemPageComponent) : Child

        data class ShopFilter(val component: ShopFilterComponent) : Child
    }

}