package com.markettwits.sportsouce.settings.internal.settings_menu.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore.Message
import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore.State

object SettingsReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.CurrentVersion -> copy(version = msg.version)
        }
    }
}