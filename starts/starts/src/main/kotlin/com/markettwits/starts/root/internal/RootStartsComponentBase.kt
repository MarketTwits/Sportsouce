package com.markettwits.starts.root.internal

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.ComponentKoinContext
import com.markettwits.selfupdater.components.notification.component.InAppNotificationComponentBase
import com.markettwits.selfupdater.components.notification.di.notificationModule
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponentBase
import com.markettwits.start.root.RootStartScreenComponentBase
import com.markettwits.start_search.root.RootStartsSearchComponentBase
import com.markettwits.starts.root.api.RootStartsComponent
import com.markettwits.starts.starts.di.startsModule
import com.markettwits.starts.starts.presentation.component.StartsScreenComponent

class RootStartsComponentBase(
    private val componentContext: ComponentContext,
    private val context: Context,
) : RootStartsComponent, ComponentContext by componentContext {

    private val koinContext = instanceKeeper.getOrCreate {
        ComponentKoinContext()
    }

    private val scope = koinContext.getOrCreateKoinScope(
        listOf(startsModule, notificationModule(context))
    )

    private val navigation = StackNavigation<RootStartsComponent.Config>()

    private val slotNavigation = SlotNavigation<RootStartsComponent.ConfigSlot>()

    override val configStack = childStack(
        source = navigation,
        serializer = RootStartsComponent.Config.serializer(),
        initialConfiguration = RootStartsComponent.Config.Starts,
        handleBackButton = true,
        childFactory = ::child,
    )
    override val childSlot: Value<ChildSlot<RootStartsComponent.ConfigSlot, RootStartsComponent.ChildSlot>> =
        childSlot(
            source = slotNavigation,
            serializer = RootStartsComponent.ConfigSlot.serializer(),
            initialConfiguration = {
                RootStartsComponent.ConfigSlot.Notification
            },
            childFactory = ::slotChild
        )

    private fun child(
        config: RootStartsComponent.Config,
        componentContext: ComponentContext,
    ): RootStartsComponent.Child =
        when (config) {
            is RootStartsComponent.Config.Start -> RootStartsComponent.Child.Start(
                RootStartScreenComponentBase(
                    context = componentContext,
                    startId = config.startId,
                    pop = navigation::pop
                )
            )

            is RootStartsComponent.Config.Starts ->
                RootStartsComponent.Child.Starts(
                    component = StartsScreenComponent(
                        componentContext = componentContext,
                        dataSource = scope.get(),
                        toDetail = {
                            navigation.push(RootStartsComponent.Config.Start(it))
                        },
                        toSearch = {
                            navigation.push(RootStartsComponent.Config.Search)
                        }
                    ),
                )

            is RootStartsComponent.Config.Search -> RootStartsComponent.Child.Search(
                RootStartsSearchComponentBase(
                    context = componentContext,
                    pop = navigation::pop
                )
            )

            is RootStartsComponent.Config.Notification -> RootStartsComponent.Child.Notification(
                SelfUpdateComponentBase(
                    componentContext = componentContext,
                    newAppVersion = config.newAppVersion,
                    storeFactory = scope.get(),
                )
            )
        }

    private fun slotChild(
        configuration: RootStartsComponent.ConfigSlot,
        componentContext: ComponentContext
    ): RootStartsComponent.ChildSlot = when (configuration) {

        is RootStartsComponent.ConfigSlot.Notification -> RootStartsComponent.ChildSlot.Notification(
            component = InAppNotificationComponentBase(
                componentContext = componentContext,
                notificationStorage = scope.get(),
                storeFactory = scope.get(),
                openFullScreen = {
                    navigation.push(RootStartsComponent.Config.Notification(it))
                }
            ),
            render = scope.get()
        )
    }
}
