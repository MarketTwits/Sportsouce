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
        currentVersion = state.newAppInfo.version,
        changes = state.newAppInfo.description,
        isSuccess = state.isSuccess,
        isFailed = state.isFailed,
        message = state.message,
        onClickStartUpdate = {
            component.obtainEvent(SelfUpdateStore.Intent.OnClickUpdate)
        },
        consumed = {
            component.obtainEvent(SelfUpdateStore.Intent.ConsumedEvent)
        }
    )
}