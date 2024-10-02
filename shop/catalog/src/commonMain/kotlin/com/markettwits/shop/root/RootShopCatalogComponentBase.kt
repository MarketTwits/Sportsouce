package com.markettwits.shop.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.shop.catalog.di.shopCatalogModule
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponent
import com.markettwits.shop.catalog.presentation.component.ShopCatalogComponentBase
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.filter.di.shopFilterModule
import com.markettwits.shop.filter.domain.models.ShopCategoryItem
import com.markettwits.shop.filter.presentation.component.ShopFilterComponent
import com.markettwits.shop.filter.presentation.component.ShopFilterComponentBase
import com.markettwits.shop.filter.presentation.store.ShopFilterStore
import com.markettwits.shop.item.di.shopItemPageModule
import com.markettwits.shop.item.presentation.component.ShopItemPageComponent
import com.markettwits.shop.item.presentation.component.ShopItemPageComponentBase
import com.markettwits.shop.search.presentation.component.ShopSearchComponent
import com.markettwits.shop.search.presentation.component.ShopSearchComponentBase
import com.markettwits.shop.search.presentation.store.ShopSearchStoreFactory


class RootShopCatalogComponentBase(
    componentContext: ComponentContext,
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
            shopFilterModule
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
                ShopCatalogComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    outputs = CardsComponentOutputsImpl()
                )
            )

            is RootShopCatalogComponent.Config.ShopItem -> RootShopCatalogComponent.Child.ShopItem(
                component = ShopItemPageComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    options = ShopItemPageComponentBase.Options(config.id),
                    outputs = ShopItemComponentOutputsImpl()
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
        }


    private inner class CardsComponentOutputsImpl : ShopCatalogComponent.Outputs {
        override fun onClickShopItem(id: String) =
            stackNavigation.push(RootShopCatalogComponent.Config.ShopItem(id))

        override fun goBack() =
            stackNavigation.pop()

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
    }

    private inner class ShopFilterComponentOutputsImpl : ShopFilterComponent.Output {

        override fun goBack() = stackNavigation.pop()

        override fun applyFilter(state: ShopFilterStore.State) {
            stackNavigation.pop {
                (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopCatalog)?.component?.obtainEvent(
                    ShopCatalogStore.Intent.ApplyFilter(state)
                )
            }
        }
    }
    private inner class ShopSearchComponentOutputsImpl : ShopSearchComponent.Outputs{
        override fun onClickGoBack() = stackNavigation.pop()

        override fun onApplyQuery(query: String) {
            stackNavigation.pop {
                (childStack.value.active.instance as? RootShopCatalogComponent.Child.ShopCatalog)?.component?.obtainEvent(
                    ShopCatalogStore.Intent.ApplyQuery(query)
                )
            }
        }
    }

}

