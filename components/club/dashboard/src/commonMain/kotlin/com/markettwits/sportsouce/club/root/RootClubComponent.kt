package com.markettwits.sportsouce.club.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
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

        @Serializable
        data class SubscriptionPricing(val subscriptionId: String) : StackConfig()

        @Serializable
        data class Schedule(val schedules: List<com.markettwits.sportsouce.club.info.domain.models.Schedule>) :
            StackConfig()
    }

    @Serializable
    sealed interface SlotConfig {
        @Serializable
        data class WorkoutRegistration(val type: RegistrationType) : SlotConfig
    }

    sealed class StackChild {
        data class Dashboard(val component: ClubDashboardComponent) : StackChild()
        data class SubscriptionPricing(val component: com.markettwits.sportsouce.club.subscription.presentation.component.SubscriptionPricingComponent) :
            StackChild()

        data class Schedule(val component: com.markettwits.sportsouce.club.schedule.presentation.component.ScheduleComponent) :
            StackChild()
    }

    @Serializable
    sealed interface SlotChild {
        data class WorkoutRegistration(val component: WorkoutRegistrationComponent) : SlotChild
    }
}