package com.markettwits.selfupdater.components.notification.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStoreFactory
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationStorage
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class InAppNotificationComponentBase(
    componentContext: ComponentContext,
    private val notificationStorage: InAppNotificationStorage,
    private val storeFactory: InAppNotificationStoreFactory,
    private val openFullScreen: (NewAppVersion) -> Unit,
) : ComponentContext by componentContext, InAppNotificationComponent {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var foregroundJob: Job? = null
    private var hasCheckedForUpdates = false

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onResume() {
                    notificationStorage.subscribe(this@InAppNotificationComponentBase)
                    foregroundJob = scope.launch {
                        // Android's application activity callback runs after the component resumes.
                        yield()
                        val intent = if (hasCheckedForUpdates) InAppNotificationStore.Intent.ResumeUpdate
                        else InAppNotificationStore.Intent.CheckForUpdates
                        hasCheckedForUpdates = true
                        store.accept(intent)
                    }
                }

                override fun onPause() {
                    foregroundJob?.cancel()
                    store.accept(InAppNotificationStore.Intent.PauseUpdate)
                    notificationStorage.unsubscribe()
                }

                override fun onDestroy() {
                    scope.cancel()
                }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<InAppNotificationStore.State> = store.stateFlow

    override fun obtainEvent(intent: InAppNotificationStore.Intent) {
        store.accept(intent)
    }

    override suspend fun onNewNotification(notification: InAppNotification) {
        store.accept(InAppNotificationStore.Intent.OnNewNotification(notification))
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is InAppNotificationStore.Label.OpenUpdateAppScreen -> openFullScreen(it.notification)
                }
            }
        }
    }
}
