package com.markettwits.club.info.presentation.component

import com.markettwits.club.info.presentation.store.ClubInfoStore
import kotlinx.coroutines.flow.StateFlow
import com.markettwits.club.info.presentation.store.ClubInfoStore.Label
import kotlinx.coroutines.flow.Flow

interface ClubInfoComponent {
    val state: StateFlow<ClubInfoStore.State>
    fun obtainEvent(intent: ClubInfoStore.Intent)
}
