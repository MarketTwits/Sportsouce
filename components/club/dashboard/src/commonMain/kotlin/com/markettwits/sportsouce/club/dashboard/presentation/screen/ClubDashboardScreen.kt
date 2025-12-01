package com.markettwits.sportsouce.club.dashboard.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.composable.SauceErrorSimpleContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.sportsouce.club.dashboard.presentation.component.ClubDashboardComponent
import com.markettwits.sportsouce.club.dashboard.presentation.components.*
import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.presentation.components.bottomsheet.MenuBottomSheetType

enum class MenuItemColor(val colors: List<Color>) {
    PLAN(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))), // Indigo to Purple
    TRAININGS(listOf(Color(0xFF10B981), Color(0xFF059669))), // Emerald gradient
    CLUB_BONUSES(listOf(Color(0xFFF59E0B), Color(0xFFEF4444))), // Amber to Red
    OUR_TEAM(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))), // Blue gradient
    SUBSCRIPTIONS(listOf(Color(0xFF8B5CF6), Color(0xFFEC4899))), // Purple to Pink
    SCHEDULE(listOf(Color(0xFF06B6D4), Color(0xFF0891B2))), // Cyan gradient
    FAQ(listOf(Color(0xFF84CC16), Color(0xFF65A30D))), // Lime gradient
    STATISTICS(listOf(Color(0xFFE11D48), Color(0xFFBE185D))) // Rose gradient
}

@Composable
fun ClubDashboardScreen(
    component: ClubDashboardComponent,
) {
    val state by component.state.collectAsState()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        PullToRefreshScreen(
            isRefreshing = state.bottomSheetData.features.isNotEmpty() && state.isLoading,
            onRefresh = { component.obtainEvent(ClubDashboardStore.Intent.RetryRequest) }
        ) { modifier ->
            LazyColumn(modifier = modifier) {
                item {
                    ClubDashboardHeader()
                }
                item {
                    AdaptivePane {
                        Column(
                            modifier = Modifier
                                .offset(y = (-100).dp)
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        ) {

                            if (state.error == null && !state.isLoading) {
                                SubscriptionInfoCard {
                                    component.obtainEvent(ClubDashboardStore.Intent.OnClickSubscriptions)
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                ClubMenuGrid(
                                    onPlanClick = {
                                        component.obtainEvent(
                                            ClubDashboardStore.Intent.OpenClubInfoDetail(
                                                MenuBottomSheetType.Plan,
                                                state.bottomSheetData
                                            )
                                        )
                                    },
                                    onTrainingsClick = {
                                        component.obtainEvent(
                                            ClubDashboardStore.Intent.OpenClubInfoDetail(
                                                MenuBottomSheetType.Trainings,
                                                state.bottomSheetData
                                            )
                                        )
                                    },
                                    onClubBonusesClick = {
                                        component.obtainEvent(
                                            ClubDashboardStore.Intent.OpenClubInfoDetail(
                                                MenuBottomSheetType.ClubBonuses,
                                                state.bottomSheetData
                                            )
                                        )
                                    },
                                    onOurTeamClick = {
                                        component.obtainEvent(
                                            ClubDashboardStore.Intent.OpenClubInfoDetail(
                                                MenuBottomSheetType.OurTeam,
                                                state.bottomSheetData
                                            )
                                        )
                                    }
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                ClubScheduleCard {
                                    component.obtainEvent(ClubDashboardStore.Intent.OnClickSchedule)
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                ClubMoreSection(
                                    onFaqClick = {
                                        component.obtainEvent(
                                            ClubDashboardStore.Intent.OpenClubInfoDetail(
                                                MenuBottomSheetType.Faq,
                                                state.bottomSheetData
                                            )
                                        )
                                    },
                                    onStatisticClick = {
                                        component.obtainEvent(
                                            ClubDashboardStore.Intent.OpenClubInfoDetail(
                                                MenuBottomSheetType.Statistics,
                                                state.bottomSheetData
                                            )
                                        )
                                    }
                                )
                            }

                            if (state.isLoading) {
                                ClubShimmerInfoCard()

                                Spacer(modifier = Modifier.height(24.dp))

                                ClubShimmerMenuGrid()

                                Spacer(modifier = Modifier.height(24.dp))

                                ClubShimmerInfoCard()

                                Spacer(modifier = Modifier.height(24.dp))

                                ShimmerMoreSection()
                            }

                            state.error?.SauceErrorSimpleContent(
                                onClickRetry = { component.obtainEvent(ClubDashboardStore.Intent.RetryRequest) }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
        BackFloatingActionButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            component.obtainEvent(ClubDashboardStore.Intent.OnClickBack)
        }

    }
}
