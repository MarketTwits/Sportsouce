package com.markettwits.schedule.schedule.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.schedule.schedule.domain.StartsSchedule

interface StartsScheduleStore : Store<StartsScheduleStore.Intent, StartsScheduleStore.State, StartsScheduleStore.Label> {

    sealed interface Intent {
        data class OnClickItem(val item : Int) : Intent
        data object Launch : Intent
        data object Back : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val starts : List<StartsSchedule> = emptyList()
    )

    sealed interface Label {
        data class OnClickItem(val item : Int) : Label
        data object Back : Label
    }
}