package com.markettwits.sportsouce.start.presentation.series.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

interface StartSeriesStore : Store<StartSeriesStore.Intent, StartSeriesStore.State, StartSeriesStore.Label> {

    sealed interface Intent {
        data object OnClickBack : Intent
        data class OnClickStart(val startId: Int) : Intent
    }

    data class State(
        val items: List<StartsListItem> = emptyList(),
        val currentStartId: Int = 0,
    )

    sealed interface Message

    sealed interface Label {
        data object OnClickBack : Label
        data class OnClickStart(val startId: Int) : Label
    }
}
