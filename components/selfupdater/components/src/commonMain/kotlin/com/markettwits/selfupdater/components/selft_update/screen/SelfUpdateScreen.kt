package com.markettwits.selfupdater.components.selft_update.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponent
import com.markettwits.selfupdater.components.selft_update.components.SelfUpdateContent
import com.markettwits.selfupdater.components.selft_update.store.store.SelfUpdateStore

@Composable
fun SelfUpdateScreen(component: SelfUpdateComponent) {
    val state by component.state.collectAsState()
    SelfUpdateContent(
        isLoading = state.isLoading,
        updateTarget = state.updateTarget,
        installerOpening = state.installerOpening,
        currentVersion = state.newAppInfo?.version ?: "",
        changes = state.newAppInfo?.description ?: "",
        isSuccess = state.isSuccess,
        isFailed = state.isFailed,
        isUpdatesAvailable = state.updatesAvailable,
        informationalOnly = state.newAppInfo?.informationalOnly == true,
        pendingMessage = state.pendingMessage,
        downloadActive = state.downloadActive,
        readyToInstall = state.readyToInstall,
        downloadPercent = state.downloadPercent,
        downloadPaused = state.downloadPaused,
        transferPercent = state.transferPercent,
        message = state.message,
        onClickGoBack = {
            component.obtainEvent(SelfUpdateStore.Intent.OnClickGoBack)
        },
        onClickStartUpdate = {
            component.obtainEvent(SelfUpdateStore.Intent.OnClickUpdate)
        },
        onClickCheck = {
            component.obtainEvent(SelfUpdateStore.Intent.OnClickCheck)
        },
        onClickInstall = { component.obtainEvent(SelfUpdateStore.Intent.OnClickInstall) },
        consumed = {
            component.obtainEvent(SelfUpdateStore.Intent.ConsumedEvent)
        }
    )
}
