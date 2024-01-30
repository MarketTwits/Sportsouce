package com.markettwits.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import detail.domain.launchService
import detail.presentation.UserDetailComponentBase
import detail.presentation.store.UserDetailStoreFactory
import di.listModule
import presentation.component.ListComponentBase


class BaseRootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, RootComponent {
    private val navigation = StackNavigation<RootComponent.Configuration>()
    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(listModule)
    )

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = RootComponent.Configuration.serializer(),
        initialStack = { listOf(RootComponent.Configuration.List) },
        childFactory = ::createChild,
    )

    override fun navigate(configuration: RootComponent.Configuration) {
        navigation.push(configuration)
    }

    private fun createChild(
        configuration: RootComponent.Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is RootComponent.Configuration.List -> RootComponent.Child.List(
                ListComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    repository = scope.get(),
                    scope = scope.get(),
                    onClickItem = {
                        navigation.push(RootComponent.Configuration.Detail(it))
                    }
                )
            )

            is RootComponent.Configuration.Detail -> RootComponent.Child.Detail(
                UserDetailComponentBase(
                    componentContext = componentContext,
                    item = configuration.item,
                    storeFactory = UserDetailStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        feature = launchService
                    ),
                    scope = scope.get(),
                    pop = navigation::pop
                )
            )
        }
}