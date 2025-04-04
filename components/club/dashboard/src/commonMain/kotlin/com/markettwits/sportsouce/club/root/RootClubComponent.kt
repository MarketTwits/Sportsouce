package com.markettwits.sportsouce.club.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponent
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.presentation.component.WorkoutRegistrationComponent
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
        data class ClubInfo(
            val index: Int,
            val items: List<com.markettwits.sportsouce.club.info.domain.models.ClubInfo>
        ) : SlotConfig

        @Serializable
        data class WorkoutRegistration(val type: RegistrationType) : SlotConfig
    }

    sealed class StackChild {
        data class Dashboard(val component: ClubDashboardComponent) : StackChild()
    }

    @Serializable
    sealed interface SlotChild {
        data class ClubInfo(val component: ClubInfoComponent) : SlotChild
        data class WorkoutRegistration(val component: WorkoutRegistrationComponent) : SlotChild
    }
}