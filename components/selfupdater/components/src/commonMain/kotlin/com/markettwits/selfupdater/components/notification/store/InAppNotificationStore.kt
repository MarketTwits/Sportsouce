package com.markettwits.selfupdater.components.notification.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Intent
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Label
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.State
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

interface InAppNotificationStore : Store<Intent, State, Label> {
    sealed class State {
        data object NotPresent : State()
        data class ShownNotification(
            val notification: InAppNotification,
            val pending: List<InAppNotification> = emptyList(),
        ) : State()
    }

    sealed interface Intent {
        data object CheckForUpdates : Intent
        data object ResumeUpdate : Intent
        data object PauseUpdate : Intent
        data class OnNotificationHidden(val notification: InAppNotification) : Intent
        data class OnNewNotification(val notification: InAppNotification) : Intent
        data class OpenNotificationScreen(val notification: InAppNotification) : Intent
    }

    sealed interface Message {
        data class Enqueue(val notification: InAppNotification) : Message
        data class Dismiss(val notification: InAppNotification) : Message
    }

    sealed interface Label {
        data class OpenUpdateAppScreen(val notification: NewAppVersion) : Label
    }

}
