package com.markettwits.start_filter.root

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
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.start_filter.start_filter.di.startFilterModule
import com.markettwits.start_filter.start_filter.presentation.StartFilterComponentBase
import com.markettwits.start_filter.start_filter.presentation.store.StartFilerStoreFactory
import com.markettwits.start_filter.starts.StartsFilteredComponentBase
import com.markettwits.start_filter.starts.store.StartsFilteredStoreFactory

class RootStartFilterComponentBase(
    context: ComponentContext,
    private val pop : () -> Unit
) : RootStartFilterComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootStartFilterComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(sportSouceNetworkModule, startFilterModule)
    )

    override val childStack: Value<ChildStack<*, RootStartFilterComponent.Child>> = childStack(
        source = navigation,
        serializer = RootStartFilterComponent.Config.serializer(),
        initialConfiguration = RootStartFilterComponent.Config.Filter,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootStartFilterComponent.Config,
        componentContext: ComponentContext,
    ): RootStartFilterComponent.Child =
        when (config) {
            is RootStartFilterComponent.Config.Filter -> RootStartFilterComponent.Child.Filter(
                StartFilterComponentBase(
                    context = componentContext,
                    storeFactory = StartFilerStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        repository = scope.get()
                    ),
                    pop = pop::invoke,
                    show = {
                        navigation.push(RootStartFilterComponent.Config.Starts(it))
                    }
                )
            )

            is RootStartFilterComponent.Config.Starts -> RootStartFilterComponent.Child.Starts(
                StartsFilteredComponentBase(
                    context = componentContext,
                    request = config.request,
                    storeFactory = StartsFilteredStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        startFilterRepository = scope.get()
                    ),
                    pop = navigation::pop,
                    onItemClick = {
                        navigation.push(RootStartFilterComponent.Config.Start(it))
                    }
                )
            )

            is RootStartFilterComponent.Config.Start -> RootStartFilterComponent.Child.Start(
                RootStartScreenComponentBase(
                    componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )
        }
}