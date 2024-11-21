package com.markettwits.club.dashboard.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.markettwits.bottom_bar.components.rememberBottomBarNestedScroll
import com.markettwits.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.club.dashboard.presentation.components.schedule.ScheduleContent
import com.markettwits.club.dashboard.presentation.components.subscriptions.SubscriptionBottomPanel
import com.markettwits.club.dashboard.presentation.components.subscriptions.SubscriptionCategoriesContent
import com.markettwits.club.dashboard.presentation.components.subscriptions.SubscriptionsContent
import com.markettwits.club.dashboard.presentation.components.title.MainDashboardContent
import com.markettwits.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.club.dashboard.presentation.components.menu.ClubMenuContent
import com.markettwits.club.dashboard.presentation.store.getSelectedSubscriptionUi
import com.markettwits.club.info.domain.models.findFirstSchedule
import com.markettwits.club.registration.domain.RegistrationType
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
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
                AnimatedContent(
                    targetState = state.subscription.items.map { it.isSelected },
                    transitionSpec = {
                        slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                    }
                ) {
                    SubscriptionsContent(
                        subscriptions = state.subscription.items
                            .filter { it.isSelected }
                            .flatMap { it.subscriptions },
                        onClick = {
                            component.obtainEvent(ClubDashboardStore.Intent.OnClickSubscriptionItem(it))
                        })
                }

                val subscription = state.subscription.getSelectedSubscriptionUi()

                SubscriptionBottomPanel(
                    state = state.subscriptionPanelState,
                    cost = subscription.subscription.price.toString(),
                    countOfMonth = subscription.monthOfCount,
                    onClickDecrease = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickDecrease)
                    },
                    onClickIncrease = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickIncrease)
                    },
                    onClickSubscribe = {
                        component.obtainEvent(ClubDashboardStore.Intent.OnClickRegistrationSubscription)
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
                            component.obtainEvent(
                                ClubDashboardStore.Intent.OnClickRegistration(
                                    RegistrationType.Schedule(it.id)
                                )
                            )
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