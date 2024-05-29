package com.markettwits.club.dashboard.presentation.dashboard.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.markettwits.bottom_bar.components.rememberBottomBarNestedScroll
import com.markettwits.club.dashboard.presentation.dashboard.component.ClubDashboardComponent
import com.markettwits.club.dashboard.presentation.dashboard.components.schedule.ScheduleContent
import com.markettwits.club.dashboard.presentation.dashboard.components.subscriptions.SubscriptionBottomPanel
import com.markettwits.club.dashboard.presentation.dashboard.components.subscriptions.SubscriptionCategoriesContent
import com.markettwits.club.dashboard.presentation.dashboard.components.subscriptions.SubscriptionsContent
import com.markettwits.club.dashboard.presentation.dashboard.components.title.MainDashboardContent
import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore
import com.markettwits.club.dashboard.presentation.informations.menu.ClubMenuContent
import com.markettwits.club.info.domain.models.findFirstSchedule
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton

@Composable
fun ClubDashboardScreen(
    component: ClubDashboardComponent
) {
    val state by component.state.collectAsState()
    Box {
        Column(
            modifier = Modifier
                .nestedScroll(
                    rememberBottomBarNestedScroll(
                        bottomBarStorage = state.bottomBarVisibilityListener
                    )
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainDashboardContent()
            if (state.isLoading) {
                LoadingFullScreen()
            }
            if (state.isError) {
                FailedScreen(
                    message = state.message
                ) {
                    component.obtainEvent(ClubDashboardStore.Intent.RetryRequest)
                }
            }
            if (state.subscription.items.isNotEmpty()) {
                SubscriptionCategoriesContent(
                    subscriptions = state.subscription.items,
                    onClick = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickKindOfSport(it))
                    }
                )
                SubscriptionsContent(
                    subscriptions = state.subscription.items
                        .filter { it.isSelected }
                        .flatMap { it.subscriptions },
                    onClick = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickSubscription(it))
                    })
                SubscriptionBottomPanel(
                    cost = state.subscription.priceInfo.totalCost.toString(),
                    countOfMonth = state.subscription.priceInfo.monthOfCount,
                    onClickDecrease = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickDecrease)
                    },
                    onClickIncrease = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickIncrease)
                    },
                    onClickSubscribe = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickRegistration())
                    }
                )
                if (state.subscription.clubInfo.isNotEmpty()) {
                    ClubMenuContent {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickInfo(it))
                    }
                }
                state.subscription.clubInfo.findFirstSchedule()?.let { schedules ->
                    ScheduleContent(
                        schedule = schedules.schedule,
                        onClick = {
                            component.obtainEvent(ClubDashboardStore.Intent.OnClickRegistration(it.id))
                        }
                    )
                }
            }
        }
        BackFloatingActionButton {
            component.obtainEvent(ClubDashboardStore.Intent.OnClickBack)
        }
    }
}