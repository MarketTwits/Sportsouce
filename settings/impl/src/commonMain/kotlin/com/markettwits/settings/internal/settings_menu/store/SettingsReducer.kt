package com.markettwits.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.Message
import com.markettwits.settings.internal.settings_menu.store.SettingsStore.State

object SettingsReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> State
        }
    }
}