package com.markettwits.sportsouce.notification.impl.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.notification.api.store.NotificationStore.*
import com.markettwits.sportsouce.notification.impl.domain.NotificationAuthRequired

internal object NotificationReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State = when (msg) {
        is Message.Count -> copy(unreadCount = msg.count)
        is Message.Error -> copy(message = msg.message)
        is Message.Feed -> copy(feed = msg.flow)
        is Message.Filter -> copy(filter = msg.filter)
        is Message.Pending -> copy(pending = msg.ids)
        is Message.ReadingAll -> copy(readingAll = msg.value)
        is Message.Refresh -> copy(refreshVersion = refreshVersion + 1, refreshing = refreshing || msg.visible)
        Message.RefreshFinished -> copy(refreshing = false)
    }
}

internal fun Throwable.notificationMessage(): String = if (this is NotificationAuthRequired) message.orEmpty()
    else "Не удалось обновить уведомления. Проверьте подключение и попробуйте ещё раз."
