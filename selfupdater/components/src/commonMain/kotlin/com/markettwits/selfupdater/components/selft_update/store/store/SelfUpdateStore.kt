package com.markettwits.selfupdater.components.selft_update.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Label
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State

interface SelfUpdateStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val isFailed: Boolean = false,
        val updatesAvailable: Boolean = false,
        val message: String = "",
        val newAppInfo: NewAppVersion?,
    )

    sealed interface Intent {
        data object OnClickUpdate : Intent
        data object ConsumedEvent : Intent
    }

    sealed interface Message {
        data class Loading(val isLoading: Boolean) : Message
        data object DownloadStarted : Message
        data class NewAppAvailable(val newAppInfo: NewAppVersion) : Message
        data class DownloadFailed(val message: String) : Message
        data object NoUpdates : Message
        data object ConsumedEvent : Message
    }

    sealed interface Label

}
