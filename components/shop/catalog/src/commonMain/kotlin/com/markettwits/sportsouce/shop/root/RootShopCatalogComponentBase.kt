package com.markettwits.sportsouce.shop.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.getOrCreateKoinScope
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponentBase
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.bottom_bar.component.storage.BottomBarStorageImpl
import com.markettwits.sportsouce.shop.cart.di.shopCartModule
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.sportsouce.shop.cart.presentation.cart.component.ShopCartComponentBase
import com.markettwits.sportsouce.shop.cart.presentation.catalog.component.ShopCartCatalogComponentBase
import com.markettwits.sportsouce.shop.cart.presentation.page.component.ShopCartPageComponent
import com.markettwits.sportsouce.shop.cart.presentation.page.component.ShopCartPageComponentBase
import com.markettwits.sportsouce.shop.catalog.di.shopCatalogModule
import com.markettwits.sportsouce.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.sportsouce.shop.catalog.presentation.component.ShopCatalogComponentBase
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.filter.di.shopFilterModule
import com.markettwits.sportsouce.shop.filter.domain.models.ShopFilterResult
import com.markettwits.sportsouce.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.sportsouce.shop.filter.presentation.component.ShopFilterComponentBase
import com.markettwits.sportsouce.shop.item.di.shopItemPageModule
import com.markettwits.sportsouce.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.sportsouce.shop.item.presentation.component.ShopItemPageComponentBase
import com.markettwits.sportsouce.shop.order.di.shopOrderModule
import com.markettwits.sportsouce.shop.order.presentation.component.ShopCreateOrderComponent
import com.markettwits.sportsouce.shop.order.presentation.component.ShopCreateOrderComponentBase
import com.markettwits.sportsouce.shop.orders.di.shopUserOrdersModule
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponent
import com.markettwits.sportsouce.shop.orders.presentation.component.ShopUserOrdersComponentBase
import com.markettwits.sportsouce.shop.search.presentation.component.ShopSearchComponent
import com.markettwits.sportsouce.shop.search.presentation.component.ShopSearchComponentBase
import com.markettwits.sportsouce.shop.search.presentation.store.ShopSearchStoreFactory

class RootShopCatalogComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
) : RootShopCatalogComponent, ComponentContext by componentContext, BottomBarComponentHandler() {

    private val stackNavigation = StackNavigation<RootShopCatalogComponent.Config>()

    private val scope = componentContext.getOrCreateKoinScope(
        listOf(
            shopItemPageModule,
            shopCatalogModule,
            shopFilterModule,
            shopCartModule,
            shopOrderModule,
            shopUserOrdersModule
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
                componentPage = ShopCatalogComponentBase(
                    componentContext = componentContext,
                    listener = BottomBarStorageImpl,
                    storeFactory = scope.get(),
                    outputs = CardsComponentOutputsImpl()
                ),
                componentCart = ShopCartCatalogComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    onClickCartHolder = {
                        stackNavigation.pushNew(RootShopCatalogComponent.Config.ShopCart)
                    }
                ),
                componentFilter = createShopFilterComponent(componentContext)
            )

            is RootShopCatalogComponent.Config.ShopItem -> RootShopCatalogComponent.Child.ShopItem(
                componentPage = ShopItemPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    options = ShopItemPageComponentBase.Options(
                        config.option.productId,
                        config.option.shopItem
                    ),
                    outputs = ShopItemComponentOutputsImpl()
                ),
                componentCart = ShopCartPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    outputs = ShopPageCartComponentOutputsImpl(),
                    shopItemCart = config.option.shopItem?.let {
                        ShopItemCart(
                            item = it,
                            count = 0
                        )
                    }
                )
            )

            is RootShopCatalogComponent.Config.ShopFilter -> RootShopCatalogComponent.Child.ShopFilter(
                createShopFilterComponent(componentContext)
            )

            is RootShopCatalogComponent.Config.ShopSearch -> RootShopCatalogComponent.Child.ShopSearch(
                component = ShopSearchComponentBase(
                    componentContext = componentContext,
                    storeFactory = ShopSearchStoreFactory(DefaultStoreFactory()),
                    outputs = ShopSearchComponentOutputsImpl(),
                    query = config.query
                )
            )

            is RootShopCatalogComponent.Config.ShopCart -> RootShopCatalogComponent.Child.ShopCart(
                component = ShopCartComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    outputs = ShopCartComponentOutputsImpl()
                )
            )

            is RootShopCatalogComponent.Config.ShopOrder -> RootShopCatalogComponent.Child.ShopOrder(
                component = ShopCreateOrderComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    option = config.option,
                    outputs = ShopOrderComponentOutputsImpl()
                )
            )

            is RootShopCatalogComponent.Config.AuthFlow -> RootShopCatalogComponent.Child.AuthFlow(
                RootAuthFlowComponentBase(
                    context = componentContext,
                    goBack = stackNavigation::pop,
                    goProfile = stackNavigation::pop
                )
            )

            is RootShopCatalogComponent.Config.ShopUserOrders -> RootShopCatalogComponent.Child.ShopUserOrders(
                ShopUserOrdersComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    outputs = ShopUserOrdersComponentOutputsImpl()
                )
            )
        }

    private fun createShopFilterComponent(componentContext: ComponentContext): ShopFilterComponent =
        ShopFilterComponentBase(
            componentContext = componentContext,
            store = scope.get(),
            output = ShopFilterComponentOutputsImpl(),
        )

    private inner class CardsComponentOutputsImpl : ShopCatalogComponent.Outputs {

        override fun onClickShopItem(item: ShopItem) =
            stackNavigation.pushNew(
                RootShopCatalogComponent.Config.ShopItem(
                    ShopItemPageComponentBase.Options(
                        item.id,
                        item
                    )
                )
            )

        override fun goBack() = pop()

        override fun goFilter() =
            stackNavigation.pushNew(RootShopCatalogComponent.Config.ShopFilter)

        override fun goSearch(query: String) {
            stackNavigation.pushNew(RootShopCatalogComponent.Config.ShopSearch(query))
        }
    }

    private inner class ShopItemComponentOutputsImpl : ShopItemPageComponent.Output {

        override fun goBack() = stackNavigation.pop()

        override fun updateItem(item: ShopItem) {
            (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopItem)?.componentCart?.updateItem(
                ShopItemCart(item)
            )
        }
    }

    private inner class ShopFilterComponentOutputsImpl : ShopFilterComponent.Output {

        override fun goBack() = stackNavigation.pop()

        override fun applyFilter(result: ShopFilterResult) {
            stackNavigation.pop {
                (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopCatalog)?.componentPage?.obtainEvent(
                    ShopCatalogStore.Intent.ApplyFilter(result)
                )
            }
        }
    }

    private inner class ShopSearchComponentOutputsImpl : ShopSearchComponent.Outputs {

        override fun goBack() {
            stackNavigation.pop()
        }

        override fun onApplyQuery(query: String) {
            stackNavigation.pop {
                (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopCatalog)?.componentPage?.obtainEvent(
                    ShopCatalogStore.Intent.ApplyQuery(query)
                )
            }
        }
    }

    private inner class ShopPageCartComponentOutputsImpl : ShopCartPageComponent.Outputs {
        override fun goToCart() {
            stackNavigation.bringToFront(RootShopCatalogComponent.Config.ShopCart)
        }
    }

    private inner class ShopCartComponentOutputsImpl : ShopCartComponent.Outputs {
        override fun goAuth() {
            stackNavigation.pushNew(RootShopCatalogComponent.Config.AuthFlow)
        }

        override fun goBack() = stackNavigation.pop()

        override fun goShopItem(shopItemCart: ShopItemCart) {
            stackNavigation.bringToFront(
                RootShopCatalogComponent.Config.ShopItem(
                    ShopItemPageComponentBase.Options(
                        shopItem = shopItemCart.item,
                        productId = shopItemCart.item.id
                    )
                )
            )
        }

        override fun goOrder(items: List<ShopItemCart>) {
            val option = ShopCreateOrderComponentBase.Option(items)
            stackNavigation.pushNew(RootShopCatalogComponent.Config.ShopOrder(option))
        }
    }

    private inner class ShopOrderComponentOutputsImpl : ShopCreateOrderComponent.Outputs {
        override fun goBack() {
            stackNavigation.pop()
        }
    }

    private inner class ShopUserOrdersComponentOutputsImpl : ShopUserOrdersComponent.Outputs {
        override fun goBack() = stackNavigation.pop()

    }

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
    }
}

