package com.markettwits.sportsouce.club.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.Message
import com.markettwits.sportsouce.club.info.presentation.store.ClubInfoStore.State

internal object ClubInfoReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.SetBottomSheetData -> copy(bottomSheetData = msg.data)
            is Message.SetSelectedTab -> copy(selectedTab = msg.tab)
            is Message.SetLoading -> copy(isLoading = msg.isLoading)
        }
    }
}