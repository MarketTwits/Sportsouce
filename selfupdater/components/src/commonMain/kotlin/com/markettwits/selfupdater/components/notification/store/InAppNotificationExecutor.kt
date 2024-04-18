package com.markettwits.selfupdater.components.notification.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.flipperdevices.selfupdater.api.SelfUpdaterApi
import com.markettwits.inappnotification.api.model.InAppNotification
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Intent
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Label
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.Message
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore.State
import kotlinx.coroutines.launch

class InAppNotificationExecutor(
    private val selfUpdaterApi: SelfUpdaterApi
) : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.OnNewNotification -> {
                dispatch(Message.UpdateState(State.ShownNotification(intent.notification)))
            }

            is Intent.OnNotificationHidden -> {
                if (intent.notification is InAppNotification.SelfUpdateReady)
                    publish(Label.OpenUpdateAppScreen(mapSelfUpdateToShared(intent.notification)))
            }

            is Intent.OpenNotificationScreen -> {
                if (intent.notification is InAppNotification.SelfUpdateReady)
                    publish(Label.OpenUpdateAppScreen(mapSelfUpdateToShared(intent.notification)))
            }
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        scope.launch {
            val result = selfUpdaterApi.startCheckUpdate(false)
        }
    }

    private fun mapSelfUpdateToShared(notification: InAppNotification.SelfUpdateReady): NewAppVersion =
        NewAppVersion(
            version = notification.actualVersion,
            description = notification.description
        )
}
