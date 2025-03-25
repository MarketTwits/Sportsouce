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
            val notification: InAppNotification
        ) : State()
    }

    sealed interface Intent {
        data class OnNotificationHidden(val notification: InAppNotification) : Intent
        data class OnNewNotification(val notification: InAppNotification) : Intent
        data class OpenNotificationScreen(val notification: InAppNotification) : Intent
    }

    sealed interface Message {
        data class UpdateState(val state: State) : Message
    }

    sealed interface Label {
        data class OpenUpdateAppScreen(val notification: NewAppVersion) : Label
    }

}
