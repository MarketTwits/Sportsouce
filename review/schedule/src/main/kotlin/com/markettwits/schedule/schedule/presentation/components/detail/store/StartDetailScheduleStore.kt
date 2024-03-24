package com.markettwits.schedule.schedule.presentation.components.detail.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Intent
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Label
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.State
import com.markettwits.starts_common.domain.StartsListItem

interface StartDetailScheduleStore : Store<Intent, State, Label> {
    data class State(val start: List<StartsListItem>)

    sealed interface Intent {
        data class OnClickItem(val id: Int) : Intent
        data object OnClickBack : Intent
    }

    sealed interface Message

    sealed interface Label {
        data class OnClickItem(val id: Int) : Label
        data object OnClickBack : Label
    }


}
