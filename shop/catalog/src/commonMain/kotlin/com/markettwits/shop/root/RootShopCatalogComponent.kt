package com.markettwits.shop.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.shop.cart.presentation.page.component.ShopCartPageComponent
import com.markettwits.shop.catalog.domain.models.ShopItem
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import com.markettwits.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.shop.search.presentation.component.ShopSearchComponent
import kotlinx.serialization.Serializable

interface RootShopCatalogComponent {

    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed interface Config {

        @Serializable
        data object ShopCatalog : Config

        @Serializable
        data class ShopItem(val shopItem: com.markettwits.shop.catalog.domain.models.ShopItem) : Config

        @Serializable
        data class ShopFilter(val state: ShopFilterStore.State?) : Config

        @Serializable
        data class ShopSearch(val query: String = "") : Config

        @Serializable
        data object ShopCart : Config

    }

    sealed interface Child {

        data class ShopCatalog(
            val componentPage: ShopCatalogComponent,
            val componentCart: ShopCartCatalogComponent
        ) : Child

        data class ShopItem(
            val componentPage: ShopItemPageComponent,
            val componentCart: ShopCartPageComponent
        ) : Child

        data class ShopFilter(val component: ShopFilterComponent) : Child

        data class ShopSearch(val component: ShopSearchComponent) : Child

        data class ShopCart(val component : ShopCartComponent) : Child
    }
}