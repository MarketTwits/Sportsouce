package com.markettwits.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.club.info.presentation.store.ClubInfoStore.State
import com.markettwits.club.info.presentation.store.ClubInfoStore.Message

internal object ClubInfoReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.ChangePage -> copy(currentIndex = msg.index)
            is Message.Loaded -> copy(info = msg.items)
        }
    }
}