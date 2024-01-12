package com.markettwits.schedule.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.schedule.schedule.di.StartScheduleComponentDependencies
import com.markettwits.schedule.schedule.di.createStartRandomModule
import com.markettwits.schedule.schedule.presentation.StartsScheduleComponentBase
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStoreFactory
import com.markettwits.start.root.RootStartScreenComponentBase

class RootStartsScheduleComponentBase(
    context: ComponentContext,
    private val dependencies: StartScheduleComponentDependencies,
    private val pop: () -> Unit
) : RootStartsScheduleComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootStartsScheduleComponent.Config>()

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        createStartRandomModule(dependencies)
    )

    override val childStack: Value<ChildStack<*, RootStartsScheduleComponent.Child>> = childStack(
        source = navigation,
        serializer = RootStartsScheduleComponent.Config.serializer(),
        initialConfiguration = RootStartsScheduleComponent.Config.Schedule,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: RootStartsScheduleComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsScheduleComponent.Child =
        when (config) {
            is RootStartsScheduleComponent.Config.Schedule -> RootStartsScheduleComponent.Child.Schedule(
                StartsScheduleComponentBase(
                    context = componentContext,
                    storeFactory = StartsScheduleStoreFactory(
                        storeFactory = DefaultStoreFactory(),
                        repository = scope.get(),
                    ),
                    onClickItem = {
                        navigation.push(RootStartsScheduleComponent.Config.Start(it))
                    },
                    pop = pop::invoke
                )
            )

            is RootStartsScheduleComponent.Config.Start -> RootStartsScheduleComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = pop::invoke
                )
            )
        }
}