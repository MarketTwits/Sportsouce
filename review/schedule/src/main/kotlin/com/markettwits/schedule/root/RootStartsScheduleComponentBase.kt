package com.markettwits.schedule.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.ComponentKoinContext
import com.markettwits.schedule.schedule.di.StartScheduleComponentDependencies
import com.markettwits.schedule.schedule.di.createStartRandomModule
import com.markettwits.schedule.schedule.presentation.component.StartsScheduleComponentBase
import com.markettwits.schedule.schedule.presentation.components.detail.component.StartDetailScheduleComponentBase
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStoreFactory
import com.markettwits.start.root.RootStartScreenComponentBase

class RootStartsScheduleComponentBase(
    context: ComponentContext,
    private val dependencies: StartScheduleComponentDependencies,
    private val pop: () -> Unit
) : RootStartsScheduleComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<RootStartsScheduleComponent.Config>()
    private val slotNavigation = SlotNavigation<RootStartsScheduleComponent.ConfigSlot>()

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
    override val childSlot: Value<ChildSlot<*, RootStartsScheduleComponent.ChildSlot>> = childSlot(
        source = slotNavigation,
        serializer = RootStartsScheduleComponent.ConfigSlot.serializer(),
        childFactory = ::childSlot
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
                        slotNavigation.activate(
                            RootStartsScheduleComponent.ConfigSlot.ScheduleDetail(
                                it
                            )
                        )
                    },
                    pop = pop::invoke
                )
            )

            is RootStartsScheduleComponent.Config.Start -> RootStartsScheduleComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )
        }

    private fun childSlot(
        configSlot: RootStartsScheduleComponent.ConfigSlot,
        componentContext: ComponentContext
    ): RootStartsScheduleComponent.ChildSlot =
        when (configSlot) {
            is RootStartsScheduleComponent.ConfigSlot.ScheduleDetail -> RootStartsScheduleComponent.ChildSlot.ScheduleDetail(
                StartDetailScheduleComponentBase(
                    componentContext = componentContext,
                    storeFactory = scope.get(),
                    start = configSlot.start,
                    back = slotNavigation::dismiss,
                    onClickStart = {
                        slotNavigation.dismiss()
                        navigation.push(RootStartsScheduleComponent.Config.Start(it))
                    }
                )
            )
        }
}