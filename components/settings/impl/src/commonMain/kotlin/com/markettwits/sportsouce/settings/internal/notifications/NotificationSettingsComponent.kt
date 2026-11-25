package com.markettwits.sportsouce.settings.internal.notifications

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.notification.api.PushNotificationPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationSettingsComponent internal constructor(
    context: ComponentContext,
    factory: NotificationSettingsStoreFactory,
    preferences: PushNotificationPreferences,
    onBack: () -> Unit,
) : ComponentContext by context {
    private val store = instanceKeeper.getStore { factory.create(preferences) }
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    internal val state = store.stateFlow
    internal fun accept(intent: NotificationSettingsStore.Intent) = store.accept(intent)

    init {
        lifecycle.doOnDestroy(scope::cancel)
        scope.launch { store.labels.collect { onBack() } }
    }
}
