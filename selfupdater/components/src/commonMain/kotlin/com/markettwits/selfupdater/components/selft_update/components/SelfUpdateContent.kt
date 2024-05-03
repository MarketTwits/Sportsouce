package com.markettwits.selfupdater.components.selft_update.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.selfupdater.components.selft_update.components.update_available.SelfUpdateAvailable
import com.markettwits.selfupdater.components.selft_update.components.update_available.SelfUpdateButton
import com.markettwits.selfupdater.components.selft_update.components.update_available.SelfUpdateStartedDialog
import com.markettwits.selfupdater.components.selft_update.components.update_available.SelfUpdateWhatsNew

@Composable
internal fun SelfUpdateContent(
    currentVersion: String,
    changes: String,
    isLoading: Boolean,
    isSuccess: Boolean,
    isFailed: Boolean,
    isUpdatesAvailable: Boolean,
    message: String,
    consumed: () -> Unit,
    onClickStartUpdate: () -> Unit,
    onClickGoBack: () -> Unit
) {
    if (isUpdatesAvailable) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelfUpdateAvailable(isAvailable = true)
            Spacer(modifier = Modifier.padding(14.dp))
            SelfUpdateWhatsNew(actualVersion = currentVersion, changes = changes)
            Spacer(modifier = Modifier.padding(14.dp))
            SelfUpdateButton(isLoading = isLoading, onClickStartUpdate = onClickStartUpdate)
        }
    }
    if (!isUpdatesAvailable && !isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            SelfUpdateAvailable(
                modifier = Modifier.align(Alignment.Center),
                isAvailable = false
            )
        }
    }

    if (isLoading) {
        LoadingFullScreen(onClickBack = {
            onClickGoBack()
        })
    }
    if (isSuccess || isFailed) {
        SelfUpdateStartedDialog(message = message, result = isSuccess) {
            consumed()
        }
    }
}