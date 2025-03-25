package com.markettwits.sportsouce.start.support.presentation.component

import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore
import kotlinx.coroutines.flow.StateFlow

interface StartSupportComponent {
    val state: StateFlow<StartSupportStore.State>
    fun obtainEvent(intent: StartSupportStore.Intent)
}
