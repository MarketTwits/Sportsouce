package com.markettwits.sportsouce.shop.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponent
import com.markettwits.sportsouce.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.sportsouce.shop.cart.presentation.catalog.component.ShopCartCatalogComponent
import com.markettwits.sportsouce.shop.cart.presentation.page.component.ShopCartPageComponent
import com.markettwits.sportsouce.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.sportsouce.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.sportsouce.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.sportsouce.shop.item.presentation.component.ShopItemPageComponentBase
import com.markettwits.sportsouce.shop.order.presentation.component.ShopCreateOrderComponent
import com.markettwits.sportsouce.shop.order.presentation.component.ShopCreateOrderComponentBase
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponent
import com.markettwits.sportsouce.shop.search.presentation.component.ShopSearchComponent
import kotlinx.serialization.Serializable

interface RootShopCatalogComponent : ComponentContext {

    val childStack: Value<ChildStack<*, Child>>

    @Serializable
    sealed interface Config {

        @Serializable
        data object AuthFlow : Config

        @Serializable
        data object ShopCatalog : Config

        @Serializable
        data class ShopItem(val option: ShopItemPageComponentBase.Options) : Config

        @Serializable
        data object ShopFilter : Config

        @Serializable
        data class ShopSearch(val query: String = "") : Config

        @Serializable
        data object ShopCart : Config

        @Serializable
        data class ShopOrder(val option: ShopCreateOrderComponentBase.Option) : Config

        @Serializable
        data object ShopUserOrders : Config

    }

    sealed interface Child {

        data class ShopCatalog(
            val componentPage: ShopCatalogComponent,
            val componentCart: ShopCartCatalogComponent,
            val componentFilter: ShopFilterComponent
        ) : Child

        data class ShopItem(
            val componentPage: ShopItemPageComponent,
            val componentCart: ShopCartPageComponent
        ) : Child

        data class ShopFilter(val component: ShopFilterComponent) : Child

        data class ShopSearch(val component: ShopSearchComponent) : Child

        data class ShopCart(val component: ShopCartComponent) : Child

        data class ShopOrder(val component: ShopCreateOrderComponent) : Child

        data class ShopUserOrders(val component: ShopUserOrdersComponent) : Child

        data class AuthFlow(val component: RootAuthFlowComponent) : Child
    }
}