package com.markettwits.sportsouce.start.search.filter.presentation.component

import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilterStore
import kotlinx.coroutines.flow.StateFlow

interface StartFilterComponent {
    fun obtainEvent(event: StartFilterStore.Intent)
    val value: StateFlow<StartFilterStore.State>
}