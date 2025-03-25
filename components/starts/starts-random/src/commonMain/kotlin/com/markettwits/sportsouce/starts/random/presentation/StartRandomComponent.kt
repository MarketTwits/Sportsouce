package com.markettwits.sportsouce.starts.random.presentation

import com.markettwits.sportsouce.starts.random.presentation.store.StartRandomStore
import kotlinx.coroutines.flow.StateFlow

interface StartRandomComponent {
    val value: StateFlow<StartRandomStore.State>
    fun obtainEvent(event: StartRandomStore.Intent)

}