package com.markettwits.sportsouce.start.presentation.series.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStore.Message
import com.markettwits.sportsouce.start.presentation.series.store.StartSeriesStore.State

object StartSeriesReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State = this
}
