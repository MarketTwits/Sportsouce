package com.markettwits.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.bottom_bar.component.component.BottomBarComponentBase
import com.markettwits.bottom_bar.component.component.BottomBarComponentHandle
import com.markettwits.bottom_bar.di.bottomBarModule
import com.markettwits.bottom_bar.model.Configuration
import com.markettwits.root_profile.RootProfileComponentBase
import com.markettwits.starts.root.RootStartsComponentBase

class RootComponentBase(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, RootComponent {

    private val scope = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }.getOrCreateKoinScope(listOf(bottomBarModule))

    private val stackNavigation = StackNavigation<RootComponent.Configuration>()

    private val slotNavigation = SlotNavigation<RootComponent.SlotConfiguration>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        serializer = RootComponent.Configuration.serializer(),
        initialStack = { listOf(RootComponent.Configuration.Review) },
        childFactory = ::createChild,
    )

    override val slotChild: Value<ChildSlot<*, RootComponent.Navigation>> = childSlot(
        source = slotNavigation,
        serializer = RootComponent.SlotConfiguration.serializer(),
        initialConfiguration = { RootComponent.SlotConfiguration.BottomBar },
        childFactory = ::createBottomBar
    )

    private fun createChild(
        configuration: RootComponent.Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is RootComponent.Configuration.Starts -> RootComponent.Child.Starts(
                RootStartsComponentBase(
                    componentContext = componentContext
                )
            )

            is RootComponent.Configuration.Profile -> RootComponent.Child.Profile(
                RootProfileComponentBase(
                    componentContext = componentContext
                )
            )

            is RootComponent.Configuration.Review -> RootComponent.Child.Review(
                RootReviewComponentBase(
                    context = componentContext
                )
            )
        }

    private fun createBottomBar(
        configuration: RootComponent.SlotConfiguration,
        componentContext: ComponentContext
    ): RootComponent.Navigation =
        when (configuration) {
            RootComponent.SlotConfiguration.BottomBar -> RootComponent.Navigation.BottomBar(
                BottomBarComponentBase(
                    componentContext = componentContext,
                    navigationComponentHandle = object : BottomBarComponentHandle {

                        override fun navigateTo(configuration: Configuration) {
                            stackNavigation.bringToFront(
                                BottomBarConfigurationMapper.map(
                                    configuration
                                )
                            )
                        }

                        override fun getActiveConfiguration(observer: (Configuration) -> Unit) {
                            childStack.subscribe {
                                observer(
                                    BottomBarConfigurationMapper.map(it.active.configuration as RootComponent.Configuration)
                                )
                            }
                        }
                    },
                    bottomBarStorage = scope.get(),
                )
            )
        }
}