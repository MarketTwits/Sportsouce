package com.markettwits.core_ui.items.extensions

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState

suspend fun SnackbarHostState.showLongMessageWithDismiss(message: String) {
    showSnackbar(
        message = message,
        duration = SnackbarDuration.Long,
        withDismissAction = true
    )
}
