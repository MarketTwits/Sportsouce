package com.markettwits.shop.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.shop.cart.di.shopCartModule
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.presentation.cart.component.ShopCartComponent
import com.markettwits.shop.cart.presentation.cart.component.ShopCartComponentBase
import com.markettwits.shop.cart.presentation.page.component.ShopCartPageComponentBase
import com.markettwits.shop.cart.presentation.catalog.component.ShopCartCatalogComponentBase
import com.markettwits.shop.cart.presentation.page.component.ShopCartPageComponent
import com.markettwits.shop.catalog.di.shopCatalogModule
import com.markettwits.shop.catalog.domain.models.ShopItem
import com.markettwits.shop.catalog.domain.models.mapToCart
import com.markettwits.shop.catalog.domain.models.mapToShopItem
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponentBase
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.di.shopFilterModule
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.shop.filter.presentation.component.ShopFilterComponentBase
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import com.markettwits.shop.item.di.shopItemPageModule
import com.markettwits.shop.item.domain.models.ShopPageItem
import com.markettwits.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.shop.item.presentation.component.ShopItemPageComponentBase
import com.markettwits.shop.search.presentation.component.ShopSearchComponent
import com.markettwits.shop.search.presentation.component.ShopSearchComponentBase
import com.markettwits.shop.search.presentation.store.ShopSearchStoreFactory
import kotlin.random.Random


class RootShopCatalogComponentBase(
    componentContext: ComponentContext,
    private val pop : () -> Unit,
) : RootShopCatalogComponent,
    ComponentContext by componentContext {

    private val stackNavigation = StackNavigation<RootShopCatalogComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(
            shopItemPageModule,
            shopCatalogModule,
            shopFilterModule,
            shopCartModule
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
                    storeFactory = scope.get(),
                    outputs = CardsComponentOutputsImpl()
                ),
                componentCart = ShopCartCatalogComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    onClickCartHolder = {
                        stackNavigation.push(RootShopCatalogComponent.Config.ShopCart)
                    }
                )
            )

            is RootShopCatalogComponent.Config.ShopItem -> RootShopCatalogComponent.Child.ShopItem(
                componentPage = ShopItemPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    options = ShopItemPageComponentBase.Options(config.shopItem.uuid),
                    outputs = ShopItemComponentOutputsImpl()
                ),
                componentCart = ShopCartPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    outputs = ShopPageCartComponentOutputsImpl(),
                    shopItemCart = config.shopItem.mapToCart()
                )
            )

            is RootShopCatalogComponent.Config.ShopFilter -> RootShopCatalogComponent.Child.ShopFilter(
                component = ShopFilterComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    output = ShopFilterComponentOutputsImpl(),
                    outerState = config.state
                )
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
        }


    private inner class CardsComponentOutputsImpl : ShopCatalogComponent.Outputs {

        override fun onClickShopItem(item: ShopItem) =
            stackNavigation.push(RootShopCatalogComponent.Config.ShopItem(item))

        override fun goBack() = pop.invoke()

        override fun goFilter(
            filterState: ShopFilterStore.State?,
        ) = stackNavigation.push(RootShopCatalogComponent.Config.ShopFilter(filterState))

        override fun goSearch(query: String) {
            stackNavigation.push(RootShopCatalogComponent.Config.ShopSearch(query))
        }

        override fun onClickCategory(categoryItem: ShopCategoryItem) {
            (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopFilter)?.component?.let {
                it.obtainEvent(ShopFilterStore.Intent.OnClickCategory(categoryItem))
                it.obtainEvent(ShopFilterStore.Intent.OnClickApplyFilter)
            }
        }


    }

    private inner class ShopItemComponentOutputsImpl : ShopItemPageComponent.Output {
        override fun goBack() = stackNavigation.pop()

        override fun updateItem(item: ShopPageItem) {
            (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopItem)?.componentCart?.updateItem(
                ShopItemCart(
                    id = Random.nextInt(),
                    uuid = item.id,
                    currentPrice = item.price.currentPrice,
                    previousPrice = item.price.previousPrice,
                    title = item.visual.displayName,
                    quantity = item.quantity,
                    imageUrl = item.visual.imageUrl
                )
            )
        }

    }

    private inner class ShopFilterComponentOutputsImpl : ShopFilterComponent.Output {

        override fun goBack() = stackNavigation.pop()

        override fun applyFilter(state: ShopFilterStore.State) {
            stackNavigation.pop {
                (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopCatalog)?.componentPage?.obtainEvent(
                    ShopCatalogStore.Intent.ApplyFilter(state)
                )
            }
        }
    }

    private inner class ShopSearchComponentOutputsImpl : ShopSearchComponent.Outputs{
        override fun onClickGoBack() = stackNavigation.pop()

        override fun onApplyQuery(query: String) {
            stackNavigation.pop {
                (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopCatalog)?.componentPage?.obtainEvent(
                    ShopCatalogStore.Intent.ApplyQuery(query)
                )
            }
        }
    }
    private inner class ShopPageCartComponentOutputsImpl : ShopCartPageComponent.Outputs{
        override fun goToCart() {
            stackNavigation.bringToFront(RootShopCatalogComponent.Config.ShopCart)
        }

    }
    private inner class ShopCartComponentOutputsImpl : ShopCartComponent.Outputs {
        override fun goBack() = stackNavigation.pop()

        override fun goShopItem(shopItemCart: ShopItemCart) {
            stackNavigation.bringToFront(RootShopCatalogComponent.Config.ShopItem(shopItemCart.mapToShopItem()))
        }
    }
}

