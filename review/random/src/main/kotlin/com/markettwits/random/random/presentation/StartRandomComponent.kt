package com.markettwits.random.random.presentation

import com.markettwits.random.random.presentation.store.StartRandomStore
import kotlinx.coroutines.flow.StateFlow

interface StartRandomComponent {
    val value: StateFlow<StartRandomStore.State>
    fun obtainEvent(event: StartRandomStore.Intent)

}