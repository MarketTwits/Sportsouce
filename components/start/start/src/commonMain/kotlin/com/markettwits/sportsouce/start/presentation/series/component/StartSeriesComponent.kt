package com.markettwits.sportsouce.start.presentation.series.component

import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStore
import kotlinx.coroutines.flow.StateFlow

interface StartSeriesComponent {
    val state: StateFlow<StartSeriesStore.State>

    fun obtainEvent(intent: StartSeriesStore.Intent)
}
