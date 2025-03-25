package com.markettwits.sportsouce.settings.internal.change_theme.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore.Message
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore.State

object ChangeThemeReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.ThemeMenu -> copy(items = msg.themeUi)
        }
    }
}