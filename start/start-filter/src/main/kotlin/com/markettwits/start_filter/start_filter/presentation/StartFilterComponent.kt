package com.markettwits.start_filter.start_filter.presentation

import com.markettwits.start_filter.start_filter.presentation.store.StartFilterStore
import kotlinx.coroutines.flow.StateFlow

interface StartFilterComponent {
    fun obtainEvent(event: StartFilterStore.Intent)
    val value: StateFlow<StartFilterStore.State>
}