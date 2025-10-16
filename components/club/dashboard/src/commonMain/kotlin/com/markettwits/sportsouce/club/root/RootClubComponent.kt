package com.markettwits.sportsouce.club.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import com.markettwits.sportsouce.club.registration.presentation.component.WorkoutRegistrationComponent
import com.markettwits.sportsouce.club.schedule.component.ScheduleComponent
import com.markettwits.sportsouce.club.subscription.component.SubscriptionPricingComponent
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
        data object Schedule : StackConfig()
    }

    @Serializable
    sealed interface SlotConfig {
        @Serializable
        data class WorkoutRegistration(val type: RegistrationType) : SlotConfig

        @Serializable
        data class ClubInfoDetail(
            val selectedTab: com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType,
            val bottomSheetData: com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore.BottomSheetData,
        ) : SlotConfig
    }

    sealed class StackChild {
        data class Dashboard(val component: ClubDashboardComponent) : StackChild()
        data class SubscriptionPricing(val component: SubscriptionPricingComponent) :
            StackChild()

        data class Schedule(val component: ScheduleComponent) :
            StackChild()
    }

    sealed interface SlotChild {
        data class WorkoutRegistration(val component: WorkoutRegistrationComponent) : SlotChild
        data class ClubInfoDetail(val component: com.markettwits.sportsouce.club.info.presentation.component.ClubInfoComponent) :
            SlotChild
    }
}