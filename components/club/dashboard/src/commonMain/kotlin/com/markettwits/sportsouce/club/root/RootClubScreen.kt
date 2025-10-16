package com.markettwits.sportsouce.club.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.club.dashboard.presentation.screen.ClubDashboardScreen
import com.markettwits.sportsouce.club.info.presentation.screen.ClubInfoDetailBottomSheet
import com.markettwits.sportsouce.club.registration.presentation.screen.WorkoutRegistrationScreen
import com.markettwits.sportsouce.club.schedule.screen.ScheduleScreen
import com.markettwits.sportsouce.club.subscription.screen.SubscriptionPricingScreen

@Composable
fun RootClubScreen(component: RootClubComponent) {

    val childStack by component.stackChildStack.subscribeAsState()
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also { child ->
        when (child) {
            is RootClubComponent.SlotChild.WorkoutRegistration -> {
                WorkoutRegistrationScreen(component = child.component)
            }

            is RootClubComponent.SlotChild.ClubInfoDetail -> {
                ClubInfoDetailBottomSheet(component = child.component)
            }
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is RootClubComponent.StackChild.Dashboard -> {
                ClubDashboardScreen(child.component)
            }

            is RootClubComponent.StackChild.SubscriptionPricing -> {
                SubscriptionPricingScreen(component = child.component)
            }

            is RootClubComponent.StackChild.Schedule -> {
                ScheduleScreen(component = child.component)
            }
        }
    }
}