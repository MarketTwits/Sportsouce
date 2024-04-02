package com.markettwits.selfupdater.components.notification.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.State
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Message

object InAppNotificationReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.UpdateState -> msg.state
        }
    }
}