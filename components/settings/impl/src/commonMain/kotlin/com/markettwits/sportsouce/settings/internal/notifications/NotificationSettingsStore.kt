package com.markettwits.sportsouce.settings.internal.notifications

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.notification.api.PushNotificationPreferences
import com.markettwits.sportsouce.notification.api.PushNotificationPreferences.State
import com.markettwits.sportsouce.settings.internal.notifications.NotificationSettingsStore.Intent
import com.markettwits.sportsouce.settings.internal.notifications.NotificationSettingsStore.Label
import kotlinx.coroutines.launch

internal interface NotificationSettingsStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data object Back : Intent
        data class SetEnabled(val enabled: Boolean) : Intent
        data object RequestPermission : Intent
        data object OpenAppSettings : Intent
        data object OpenChannelSettings : Intent
    }

    enum class Label { Back }
}

internal class NotificationSettingsStoreFactory(private val factory: StoreFactory) {
    fun create(preferences: PushNotificationPreferences): NotificationSettingsStore =
        object : NotificationSettingsStore, Store<Intent, State, Label> by factory.create(
            name = "NotificationSettingsStore",
            initialState = preferences.settings.value,
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { NotificationSettingsExecutor(preferences) },
            reducer = { state -> state },
        ) {}
}

private class NotificationSettingsExecutor(private val preferences: PushNotificationPreferences) :
    CoroutineExecutor<Intent, Unit, State, State, Label>() {
    override fun executeAction(action: Unit) {
        scope.launch { preferences.settings.collect(::dispatch) }
    }

    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.Back -> publish(Label.Back)
            is Intent.SetEnabled -> preferences.setEnabled(intent.enabled)
            Intent.RequestPermission -> preferences.request()
            Intent.OpenAppSettings -> preferences.openAppSettings()
            Intent.OpenChannelSettings -> preferences.openChannelSettings()
        }
    }
}
