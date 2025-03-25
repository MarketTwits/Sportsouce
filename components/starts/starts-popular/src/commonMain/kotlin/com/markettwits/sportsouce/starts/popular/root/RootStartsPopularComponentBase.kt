package com.markettwits.sportsouce.starts.popular.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase
import com.markettwits.sportsouce.starts.popular.di.popularStartsModule
import com.markettwits.sportsouce.starts.popular.presentation.component.StartsPopularComponentBase

class RootStartsPopularComponentBase(
    context: ComponentContext,
    private val pop: () -> Unit
) : RootStartsPopularComponent, ComponentContext by context {
    private val navigation = StackNavigation<RootStartsPopularComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(popularStartsModule)
    )

    override val childStack: Value<ChildStack<*, RootStartsPopularComponent.Child>> = childStack(
        source = navigation,
        serializer = RootStartsPopularComponent.Config.serializer(),
        initialConfiguration = RootStartsPopularComponent.Config.Popular,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootStartsPopularComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsPopularComponent.Child =
        when (config) {
            is RootStartsPopularComponent.Config.Popular -> RootStartsPopularComponent.Child.Popular(
                StartsPopularComponentBase(
                    context = componentContext,
                    storeFactory = scope.get(),
                    pop = pop,
                    start = {
                        navigation.pushNew(RootStartsPopularComponent.Config.Start(it))
                    },
                )
            )

            is RootStartsPopularComponent.Config.Start -> RootStartsPopularComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )
        }
}