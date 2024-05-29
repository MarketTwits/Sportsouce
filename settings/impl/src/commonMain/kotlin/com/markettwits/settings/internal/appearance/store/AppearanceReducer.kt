package com.markettwits.settings.internal.appearance.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.settings.internal.appearance.store.AppearanceStore.Message
import com.markettwits.settings.internal.appearance.store.AppearanceStore.State

internal object AppearanceReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.ThemeMenu -> copy(items = msg.item)
        }
    }
}