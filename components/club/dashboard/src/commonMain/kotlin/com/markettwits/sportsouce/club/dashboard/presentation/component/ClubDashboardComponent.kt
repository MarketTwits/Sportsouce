package com.markettwits.sportsouce.club.dashboard.presentation.component

import com.markettwits.sportsouce.club.dashboard.presentation.store.ClubDashboardStore
import com.markettwits.sportsouce.club.info.domain.models.ClubInfo
import com.markettwits.sportsouce.club.registration.domain.RegistrationType
import kotlinx.coroutines.flow.StateFlow


interface ClubDashboardComponent {
    val state: StateFlow<ClubDashboardStore.State>
    fun obtainEvent(intent: ClubDashboardStore.Intent)

    sealed interface Output {
        data object Dismiss : Output
        data class GoInfo(val index: Int, val clubInfo: List<ClubInfo>) : Output
        data class Subscription(val type: RegistrationType) : Output
    }
}