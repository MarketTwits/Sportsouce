package com.markettwits.core_ui.items.screens

import androidx.compose.ui.window.Dialog

@androidx.compose.runtime.Composable
actual fun SauceDialog(
    onDismissRequest: () -> Unit,
    properties: androidx.compose.ui.window.DialogProperties,
    content: @androidx.compose.runtime.Composable (() -> Unit),
) {
    Dialog(onDismissRequest = onDismissRequest, properties = properties, content = content)
}