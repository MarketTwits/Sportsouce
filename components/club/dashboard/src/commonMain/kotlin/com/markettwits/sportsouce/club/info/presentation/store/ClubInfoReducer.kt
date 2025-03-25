package com.markettwits.sportsouce.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.Message
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.State

internal object ClubInfoReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.ChangePage -> copy(currentIndex = msg.index)
            is Message.Loaded -> copy(info = msg.items)
        }
    }
}