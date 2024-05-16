package com.markettwits.club.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponent
import com.markettwits.club.info.presentation.component.ClubInfoComponent
import kotlinx.serialization.Serializable

interface RootClubComponent {
    val stackChildStack: Value<ChildStack<*, StackChild>>
    val childSlot: Value<ChildSlot<*, SlotChild>>

    @Serializable
    sealed class StackConfig {
        @Serializable
        data object Dashboard : StackConfig()

    }

    @Serializable
    sealed interface SlotConfig {
        @Serializable
        data class ClubInfo(val index: Int) : SlotConfig
    }

    sealed class StackChild {
        data class Dashboard(val component: ClubDashboardComponent) : StackChild()
    }

    @Serializable
    sealed interface SlotChild {
        data class ClubInfo(val component: ClubInfoComponent) : SlotChild
    }
}