package com.markettwits.club.dashboard.presentation.dashboard.component

import com.markettwits.club.dashboard.presentation.dashboard.store.ClubDashboardStore
import kotlinx.coroutines.flow.StateFlow


interface ClubDashboardComponent {
    val state: StateFlow<ClubDashboardStore.State>
    fun obtainEvent(intent: ClubDashboardStore.Intent)
}