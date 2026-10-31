package com.markettwits.selfupdater.components.selft_update.store.store

import com.markettwits.selfupdater.models.UpdateTarget
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Intent
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.Label
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore.State

interface SelfUpdateStore : Store<Intent, State, Label> {
    data class State(
        val updateTarget: UpdateTarget = UpdateTarget.Android,
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
        val installerOpening: Boolean = false,
        val isFailed: Boolean = false,
        val updatesAvailable: Boolean = false,
        val pendingMessage: String? = null,
        val downloadActive: Boolean = false,
        val readyToInstall: Boolean = false,
        val downloadPercent: Int? = null,
        val downloadPaused: Boolean = false,
        val transferPercent: Int? = null,
        val message: String = "",
        val newAppInfo: NewAppVersion?,
    )

    sealed interface Intent {
        data object OnClickGoBack : Intent
        data object OnClickUpdate : Intent
        data object OnClickCheck : Intent
        data object OnClickInstall : Intent
        data object ConsumedEvent : Intent
        data object ScreenResumed : Intent
        data object ScreenPaused : Intent
    }

    sealed interface Message {
        data class Loading(val isLoading: Boolean) : Message
        data class OpeningInstaller(val opening: Boolean) : Message
        data class TransferProgress(val percent: Int?) : Message
        data object DownloadStarted : Message
        data class DownloadPending(val ready: Boolean, val percent: Int? = null, val paused: Boolean = false, val showDialog: Boolean = true) : Message
        data class Status(val text: String) : Message
        data class ReleaseInfo(val info: NewAppVersion) : Message
        data class NewAppAvailable(val newAppInfo: NewAppVersion) : Message
        data class DownloadFailed(val message: String) : Message
        data object NoUpdates : Message
        data object ConsumedEvent : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}
