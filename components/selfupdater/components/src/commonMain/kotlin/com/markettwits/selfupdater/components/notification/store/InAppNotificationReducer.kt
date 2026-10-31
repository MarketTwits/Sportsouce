package com.markettwits.selfupdater.components.notification.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Message
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.State
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

object InAppNotificationReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Enqueue -> if (msg.notification is InAppNotification.SelfUpdateCleared) {
                val releaseId = msg.notification.releaseId
                when (this) {
                    State.NotPresent -> this
                    is State.ShownNotification -> {
                        fun matches(value: InAppNotification): Boolean = value is InAppNotification.SelfUpdateReady && value.releaseId == releaseId
                        val remaining = pending.filterNot(::matches)
                        if (!matches(notification)) copy(pending = remaining)
                        else if (remaining.isEmpty()) State.NotPresent
                        else State.ShownNotification(remaining.first(), remaining.drop(1))
                    }
                }
            } else when (this) {
                State.NotPresent -> State.ShownNotification(msg.notification)
                is State.ShownNotification -> {
                    val terminal = msg.notification is InAppNotification.SelfUpdateError ||
                        (msg.notification is InAppNotification.SelfUpdateReady && msg.notification.readyToInstall)
                    val ready = (msg.notification as? InAppNotification.SelfUpdateReady)?.takeIf { it.readyToInstall }
                    val webOffer = (msg.notification as? InAppNotification.SelfUpdateReady)?.takeIf {
                        it.informationalOnly && it.releaseId?.startsWith("web:") == true
                    }
                    val transfer = msg.notification == InAppNotification.SelfUpdateStarted || msg.notification is InAppNotification.SelfUpdateProgress
                    fun superseded(old: InAppNotification): Boolean =
                        (webOffer != null && old is InAppNotification.SelfUpdateReady && old.informationalOnly &&
                            old.releaseId?.startsWith("web:") == true) ||
                        (old is InAppNotification.SelfUpdateError && (transfer || terminal)) ||
                        (transfer && old is InAppNotification.SelfUpdateReady && !old.readyToInstall) ||
                        ((terminal || msg.notification is InAppNotification.SelfUpdateProgress) &&
                            (old == InAppNotification.SelfUpdateStarted || old is InAppNotification.SelfUpdateProgress)) ||
                            (ready?.releaseId != null && old is InAppNotification.SelfUpdateReady &&
                                !old.readyToInstall && old.releaseId == ready.releaseId)
                    val waiting = pending.filterNot(::superseded)
                    if (superseded(notification)) {
                        val next = waiting + msg.notification
                        State.ShownNotification(next.first(), next.drop(1).takeLast(MAX_PENDING))
                    } else copy(pending = (waiting + msg.notification).takeLast(MAX_PENDING))
                }
            }
            is Message.Dismiss -> when (this) {
                State.NotPresent -> this
                is State.ShownNotification -> if (notification != msg.notification) this
                    else if (pending.isEmpty()) State.NotPresent
                    else State.ShownNotification(pending.first(), pending.drop(1))
            }
        }
    }

    private const val MAX_PENDING = 32
}
