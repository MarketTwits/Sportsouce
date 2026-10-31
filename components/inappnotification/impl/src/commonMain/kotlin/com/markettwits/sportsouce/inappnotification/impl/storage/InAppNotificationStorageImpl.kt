package com.markettwits.sportsouce.inappnotification.impl.storage

import com.markettwits.sportsouce.inappnotification.api.InAppNotificationListener
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/** Serializes subscription and notification events on the UI dispatcher. */
class InAppNotificationStorageImpl(
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
) : InAppNotificationStorage {
    private sealed interface Event {
        data class Subscribe(val listener: InAppNotificationListener) : Event
        data object Unsubscribe : Event
        data class Add(val notification: InAppNotification) : Event
    }

    private val events = Channel<Event>(Channel.UNLIMITED)

    init {
        scope.launch {
            val pending = ArrayDeque<InAppNotification>()
            var listener: InAppNotificationListener? = null
            for (event in events) {
                when (event) {
                    is Event.Subscribe -> listener = event.listener
                    Event.Unsubscribe -> listener = null
                    is Event.Add -> {
                        // Bound retained notifications if the application has no active UI.
                        if (pending.size == MAX_PENDING) pending.removeFirst()
                        pending.addLast(event.notification)
                    }
                }
                while (listener != null && pending.isNotEmpty()) {
                    val activeListener = listener
                    try {
                        activeListener.onNewNotification(pending.first())
                        pending.removeFirst()
                    } catch (cancelled: CancellationException) {
                        throw cancelled
                    } catch (_: Exception) {
                        // Keep the event for the next subscriber rather than losing it.
                        listener = null
                    }
                }
            }
        }
    }

    override fun subscribe(listener: InAppNotificationListener) {
        events.trySend(Event.Subscribe(listener))
    }

    override fun unsubscribe() {
        events.trySend(Event.Unsubscribe)
    }

    override fun addNotification(notification: InAppNotification) {
        events.trySend(Event.Add(notification))
    }

    private companion object {
        const val MAX_PENDING = 32
    }
}
