package com.markettwits.selfupdater.components.notification.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.inappnotification.api.InAppNotificationStorage
import com.markettwits.inappnotification.api.model.InAppNotification
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InAppNotificationComponentBase(
    componentContext: ComponentContext,
    private val notificationStorage: InAppNotificationStorage,
    private val storeFactory: InAppNotificationStoreFactory,
    private val openFullScreen: (NewAppVersion) -> Unit,
) : ComponentContext by componentContext, InAppNotificationComponent {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onResume() {
                    notificationStorage.subscribe(this@InAppNotificationComponentBase)
                }

                override fun onPause() {
                    notificationStorage.unsubscribe()
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