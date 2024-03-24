package com.markettwits.schedule.schedule.presentation.components.detail.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.Message
import com.markettwits.schedule.schedule.presentation.components.detail.store.StartDetailScheduleStore.State

object StartDetailScheduleReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}