package com.markettwits.start_search.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.start_filter.root.RootStartFilterComponentBase
import com.markettwits.start_search.root.di.rootStartsSearchModule
import com.markettwits.start_search.search.presentation.component.StartsSearchComponentBase


class RootStartsSearchComponentBase(
    context: ComponentContext,
    private val pop: () -> Unit,
) : RootStartsSearchComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootStartsSearchComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(rootStartsSearchModule)
    )

    override val childStack: Value<ChildStack<*, RootStartsSearchComponent.Child>> = childStack(
        source = navigation,
        serializer = RootStartsSearchComponent.Config.serializer(),
        initialConfiguration = RootStartsSearchComponent.Config.Search,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootStartsSearchComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsSearchComponent.Child =
        when (config) {

            is RootStartsSearchComponent.Config.Start -> RootStartsSearchComponent.Child.Start(
                RootStartScreenComponentBase(
                    componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )

            is RootStartsSearchComponent.Config.Filter -> RootStartsSearchComponent.Child.Filter(
                RootStartFilterComponentBase(
                    context = componentContext,
                    dependencies = scope.get(),
                    pop = navigation::pop
                )
            )

            is RootStartsSearchComponent.Config.Search -> RootStartsSearchComponent.Child.Search(
                StartsSearchComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    back = pop::invoke,
                    filter = {
                        navigation.push(RootStartsSearchComponent.Config.Filter)
                    },
                    start = {
                        navigation.push(RootStartsSearchComponent.Config.Start(it))
                    }
                )
            )
        }

}