package com.markettwits.sportsouce.club.info.presentation.component

import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore
import kotlinx.coroutines.flow.StateFlow

interface ClubInfoComponent {
    val state: StateFlow<ClubInfoStore.State>
    fun obtainEvent(intent: ClubInfoStore.Intent)
}
