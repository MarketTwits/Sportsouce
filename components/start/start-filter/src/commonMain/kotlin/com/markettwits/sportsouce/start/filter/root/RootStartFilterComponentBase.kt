package com.markettwits.sportsouce.start.filter.root

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
import com.markettwits.sportsouce.start.filter.start_filter.di.StartsFilterDependencies
import com.markettwits.sportsouce.start.filter.start_filter.di.createStartFilterModules
import com.markettwits.sportsouce.start.filter.start_filter.presentation.component.StartFilterComponentBase
import com.markettwits.sportsouce.start.filter.start_filter.presentation.store.StartFilerStoreFactory
import com.markettwits.sportsouce.start.filter.starts.StartsFilteredComponentBase
import com.markettwits.sportsouce.start.filter.starts.store.StartsFilteredStoreFactory
import com.markettwits.sportsouce.start.root.RootStartScreenComponentBase


class RootStartFilterComponentBase(
    context: ComponentContext,
    private val dependencies: StartsFilterDependencies,
    private val pop: () -> Unit
) : RootStartFilterComponent,
    ComponentContext by context {

    private val navigation = StackNavigation<RootStartFilterComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        createStartFilterModules(dependencies)
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
                    show = { filter, sorted ->
                        navigation.push(RootStartFilterComponent.Config.Starts(filter, sorted))
                    })
            )

            is RootStartFilterComponent.Config.Starts -> RootStartFilterComponent.Child.Starts(
                StartsFilteredComponentBase(
                    context = componentContext,
                    request = config.request,
                    startFilterSorted = config.sorted,
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