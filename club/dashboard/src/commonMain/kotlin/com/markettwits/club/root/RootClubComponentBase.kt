package com.markettwits.club.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.markettwits.club.dashboard.di.clubDashboardModule
import com.markettwits.club.dashboard.di.createDashboardComponent
import com.markettwits.club.info.di.clubInfoModule
import com.markettwits.club.info.di.createClubInfoComponent
import com.markettwits.getOrCreateKoinScope

class RootClubComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
) : ComponentContext by componentContext, RootClubComponent {

    private val scope = getOrCreateKoinScope(
        listOf(clubDashboardModule, clubInfoModule)
    )

    private val stackNavigation = StackNavigation<RootClubComponent.StackConfig>()

    private val slotNavigation = SlotNavigation<RootClubComponent.SlotConfig>()

    override val childSlot: Value<ChildSlot<*, RootClubComponent.SlotChild>> =
        childSlot(
            source = slotNavigation,
            serializer = RootClubComponent.SlotConfig.serializer(),
            handleBackButton = true,
            childFactory = ::childSlot
        )

    override val stackChildStack: Value<ChildStack<*, RootClubComponent.StackChild>> =
        childStack(
            source = stackNavigation,
            serializer = RootClubComponent.StackConfig.serializer(),
            initialConfiguration = RootClubComponent.StackConfig.Dashboard,
            handleBackButton = true,
            childFactory = ::childStack,
        )

    private fun childSlot(
        config: RootClubComponent.SlotConfig,
        componentContext: ComponentContext,
    ): RootClubComponent.SlotChild =
        when (config) {
            is RootClubComponent.SlotConfig.ClubInfo -> RootClubComponent.SlotChild.ClubInfo(
                scope.createClubInfoComponent(
                    index = config.index,
                    goBack = {
                        slotNavigation.dismiss()
                    },
                )
            )
        }


    private fun childStack(
        stackConfig: RootClubComponent.StackConfig,
        componentContext: ComponentContext,
    ): RootClubComponent.StackChild =
        when (stackConfig) {
            RootClubComponent.StackConfig.Dashboard -> RootClubComponent.StackChild.Dashboard(
                scope.createDashboardComponent(goBack = pop::invoke, goInfo = {
                    slotNavigation.activate(RootClubComponent.SlotConfig.ClubInfo(it))
                })
            )
        }
}