package com.markettwits.shop.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.shop.catalog.di.shopCatalogModule
import com.markettwits.shop.catalog.presentation.component.CardsComponent
import com.markettwits.shop.catalog.presentation.component.CardsComponentBase
import com.markettwits.shop.item.di.shopItemPageModule
import com.markettwits.shop.item.presentation.component.ShopItemPageComponentBase


class RootShopCatalogComponentBase(componentContext: ComponentContext) : RootShopCatalogComponent,
    ComponentContext by componentContext {

    private val stackNavigation = StackNavigation<RootShopCatalogComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(
            shopItemPageModule,
            shopCatalogModule
        )
    )

    override val childStack: Value<ChildStack<*, RootShopCatalogComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = RootShopCatalogComponent.Config.serializer(),
        initialConfiguration = RootShopCatalogComponent.Config.ShopCatalog,
        handleBackButton = true,
        childFactory = ::child,
    )


    private fun child(
        config: RootShopCatalogComponent.Config,
        componentContext: ComponentContext,
    ): RootShopCatalogComponent.Child =
        when (config) {
            is RootShopCatalogComponent.Config.ShopCatalog -> RootShopCatalogComponent.Child.ShopCatalog(
                CardsComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    outputs = CardsComponentOutputsImpl()
                )
            )

            is RootShopCatalogComponent.Config.ShopItem -> RootShopCatalogComponent.Child.ShopItem(
                component = ShopItemPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    options = ShopItemPageComponentBase.Options(config.id)
                )
            )
        }

    inner class CardsComponentOutputsImpl : CardsComponent.Outputs {
        override fun onClickShopItem(id: String) {
            stackNavigation.push(RootShopCatalogComponent.Config.ShopItem(id))
        }

        override fun goBack() {
            stackNavigation.pop()
        }
    }

}

